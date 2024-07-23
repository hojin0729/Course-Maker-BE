package coursemaker.coursemaker.api.tourApi.service;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.busanApi.service.BusanApiService;
import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.repository.TourApiRepository;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    @Value("${tourapi.serviceKey}")
    private String serviceKey;

    @Value("${tourapi.baseUrl}")
    private String baseUrl;

    @Value("${tourapi.detailCommonUrl}")
    private String detailCommonUrl;

    @Value("${tourapi.disableTourUrl}")
    private String disableTourUrl;

    @Value("${tourapi.disableUpdateUrl}")
    private String disableUpdateUrl;

    private final WebClient.Builder webClientBuilder;
    private final TourApiRepository tourApiRepository;
    private final DestinationRepository destinationRepository;
    private final BusanApiService busanApiService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public TourApiResponse updateAndGetTour() {
        CompletableFuture<TourApiResponse> initialUpdateFuture = CompletableFuture.supplyAsync(this::initialUpdate, executorService);

        CompletableFuture<BusanApiResponse> busanApiUpdateFuture = CompletableFuture.supplyAsync(busanApiService::initialUpdate, executorService);

        initialUpdateFuture
                .thenComposeAsync(response -> updateDisabledTours()
                        .thenRunAsync(this::handleMissingDisabledData, executorService)
                        .thenComposeAsync(aVoid -> {
                            List<CompletableFuture<Void>> updateFutures = tourApiRepository.findAll().stream()
                                    .map(tour -> CompletableFuture.runAsync(() -> updateCommonData(tour.getId()), executorService))
                                    .collect(Collectors.toList());
                            return CompletableFuture.allOf(updateFutures.toArray(new CompletableFuture[0]));
                        }, executorService), executorService)
                .thenRunAsync(this::updateMissingData, executorService)
                .thenRunAsync(this::convertAndSaveToDestination, executorService)
                .join();

        busanApiUpdateFuture
                .thenRunAsync(busanApiService::busanConvertAndSaveToDestination, executorService)
                .join();

        return initialUpdateFuture.join();
    }

    private TourApiResponse initialUpdate() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("numOfRows", 2300)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("areaCode", 6)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();
        try {
            TourApiResponse response = webClientBuilder.build().get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(status -> status.value() >= 400, clientResponse -> {
                        log.error("Error response code: {}", clientResponse.statusCode().value());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Error body: {}", errorBody);
                                    return Mono.error(new RuntimeException("Error response from API: " + clientResponse.statusCode().value() + " " + errorBody));
                                });
                    })
                    .bodyToMono(TourApiResponse.class)
                    .doOnNext(res -> log.debug("Received response: {}", res))
                    .block();

            if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                List<TourApi> tourList = response.getResponse().getBody().getItems().getItem().stream()
                        .filter(item -> !isExcludedCat1(item.getCat1())) // cat1 필터링
                        .filter(item -> !isExcludedCat3(item.getCat3())) // cat3 필터링
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());
                synchronized (this) {
                    tourList.forEach(this::saveOrUpdateTour);
                }
            }
            return response;
        } catch (Exception e) {
            log.error("Exception occurred while updating tours: ", e);
            throw new RuntimeException("Failed to update tours", e);
        }
    }

    private boolean isExcludedCat1(String cat1) {
        List<String> excludedCat1List = List.of(
                "C01", "B02", "A04", "A05"
        );
        return excludedCat1List.contains(cat1);
    }

    private boolean isExcludedCat3(String cat3) {
        List<String> excludedCat3List = List.of(
                "A02080200", "A02080300", "A02080400", "A02080500", "A02080600",
                "A02080800", "A02080900", "A02081000", "A02081100", "A02060900",
                "A02061000", "A02061200", "A02061300", "A02061400"
        );
        return excludedCat3List.contains(cat3);
    }

    private CompletableFuture<Void> updateDisabledTours() {
        return CompletableFuture.runAsync(() -> {
            URI uri = UriComponentsBuilder.fromHttpUrl(disableTourUrl)
                    .queryParam("numOfRows", 2300)
                    .queryParam("pageNo", 1)
                    .queryParam("MobileOS", "WIN")
                    .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                    .queryParam("_type", "json")
                    .queryParam("areaCode", 6)
                    .queryParam("serviceKey", serviceKey)
                    .build(true)
                    .toUri();

            try {
                TourApiResponse response = webClientBuilder.build().get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(TourApiResponse.class)
                        .timeout(Duration.ofSeconds(10))
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
                        .block();

                if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                    List<Long> disabledContentIds = response.getResponse().getBody().getItems().getItem().stream()
                            .map(TourApiResponse.Item::getContentid)
                            .collect(Collectors.toList());

                    List<TourApi> allTours = tourApiRepository.findAll();
                    allTours.forEach(tour -> {
                        if (disabledContentIds.contains(tour.getContentid())) {
                            tour.setDisabled(1L);
                        } else {
                            tour.setDisabled(0L);
                        }
                        tourApiRepository.save(tour);
                    });
                }
            } catch (Exception e) {
                log.error("Error updating disabled tours: ", e);
            }
        }, executorService);
    }

    private void handleMissingDisabledData() {
        List<TourApi> allTours = tourApiRepository.findAll();
        for (TourApi tour : allTours) {
            if (tour.getDisabled() == null) {
                retryUpdateDisabledData(tour.getContentid());
            }
        }
    }

    private void retryUpdateDisabledData(long contentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(disableUpdateUrl)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("contentId", contentId)
                .build(true)
                .toUri();

        try {
            TourApiResponse response = webClientBuilder.build().get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(TourApiResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
                    .block();

            if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                response.getResponse().getBody().getItems().getItem().forEach(item -> {
                    Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
                    tourApiOptional.ifPresent(tourApi -> {
                        synchronized (this) {
                            tourApi.setDisabled(1L);
                            tourApiRepository.save(tourApi);
                        }
                    });
                });
            } else {
                log.error("Invalid response for contentId: {}", contentId);
            }
        } catch (Exception e) {
            log.error("Exception occurred while retrying disabled data for contentId: {}", contentId, e);
        }
    }

    private void updateCommonData(long id) {
        Optional<TourApi> getTourApi = tourApiRepository.findById(id);
        if (getTourApi.isPresent()) {
            long contentId = getTourApi.get().getContentid();
            int pageNo = 1;
            boolean moreData = true;

            while (moreData) {
                URI uri = UriComponentsBuilder.fromHttpUrl(detailCommonUrl)
                        .queryParam("numOfRows", 100) // 한 페이지에 가져올 데이터 수
                        .queryParam("pageNo", pageNo)
                        .queryParam("MobileOS", "WIN")
                        .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                        .queryParam("_type", "json")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("defaultYN", "Y")
                        .queryParam("overviewYN", "Y")
                        .queryParam("contentId", contentId)
                        .build(true)
                        .toUri();

                try {
                    TourApiResponse response = webClientBuilder.build().get()
                            .uri(uri)
                            .retrieve()
                            .onStatus(status -> status.value() >= 400, clientResponse -> {
                                return clientResponse.bodyToMono(String.class)
                                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error response from API: " + clientResponse.statusCode().value() + " " + errorBody)));
                            })
                            .bodyToMono(TourApiResponse.class)
                            .timeout(Duration.ofSeconds(10))
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
                            .block();

                    if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                        List<TourApiResponse.Item> items = response.getResponse().getBody().getItems().getItem();
                        items.forEach(item -> {
                            Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
                            tourApiOptional.ifPresent(tourApi -> {
                                synchronized (this) {
                                    tourApi.setHomepage(item.getHomepage());
                                    tourApi.setOverview(item.getOverview());
                                    tourApiRepository.save(tourApi);
                                }
                            });
                        });

                        if (items.size() < 100) {
                            moreData = false; // 더 이상 데이터가 없으면 루프 종료
                        } else {
                            pageNo++; // 다음 페이지로 이동
                        }
                    } else {
                        moreData = false; // 응답이 없거나 유효하지 않으면 루프 종료
                    }
                } catch (Exception e) {
                    moreData = false; // 예외 발생 시 루프 종료
                }
            }
        }
    }

    private void updateMissingData() {
        List<TourApi> missingDataTours = tourApiRepository.findAll().stream()
                .filter(tour -> tour.getOverview() == null)
                .collect(Collectors.toList());

        for (TourApi tour : missingDataTours) {
            retryUpdateCommonData(tour.getContentid());
        }
    }

    private void retryUpdateCommonData(long contentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(detailCommonUrl)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("defaultYN", "Y")
                .queryParam("overviewYN", "Y")
                .queryParam("contentId", contentId)
                .build(true)
                .toUri();

        try {
            TourApiResponse response = webClientBuilder.build().get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(TourApiResponse.class)
                    .timeout(Duration.ofSeconds(10)) // 타임아웃 설정
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
                    .block();

            if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                response.getResponse().getBody().getItems().getItem().forEach(item -> {
                    Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
                    tourApiOptional.ifPresent(tourApi -> {
                        synchronized (this) {
                            tourApi.setHomepage(item.getHomepage());
                            tourApi.setOverview(item.getOverview());
                            tourApiRepository.save(tourApi);
                        }
                    });
                });
            } else {
                log.error("Invalid response for contentId: {}", contentId);
            }
        } catch (Exception e) {
            log.error("Exception occurred while retrying common data for contentId: {}", contentId, e);
        }
    }

    @Override
    public List<TourApi> getAllTours() {
        return tourApiRepository.findAll();
    }

    @Override
    public Optional<TourApi> getTourById(Long id) {
        return tourApiRepository.findById(id);
    }

    private void convertAndSaveToDestination() {
        List<TourApi> tourApis = tourApiRepository.findAll();
        for (TourApi tourApi : tourApis) {
            // Destination 테이블에 이미 해당 TourApi의 contentid가 있는지 확인
            Optional<Destination> existingDestination = destinationRepository.findByContentId(tourApi.getContentid());
            if (existingDestination.isEmpty()) {
                // Destination 테이블에 해당 항목이 없으면 새로 저장
                Destination destination = new Destination();
                destination.setName(tourApi.getTitle());
                destination.setPictureLink(tourApi.getFirstimage());
                destination.setViews(0);
                destination.setContent(tourApi.getOverview());
                destination.setLocation(tourApi.getAddr1());
                destination.setLongitude(tourApi.getMapx());
                destination.setLatitude(tourApi.getMapy());
                destination.setDisabled(tourApi.getDisabled());
                destination.setContentId(tourApi.getContentid());

                // createdAt과 updatedAt은 String에서 LocalDateTime으로 변환하여 설정
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime createdAt = LocalDateTime.parse(tourApi.getCreatedtime(), formatter);
                LocalDateTime updatedAt = LocalDateTime.parse(tourApi.getModifiedtime(), formatter);
                destination.setCreatedAt(createdAt);
                destination.setUpdatedAt(updatedAt);

                destinationRepository.save(destination);
            }
        }
    }

    private TourApi convertToEntity(TourApiResponse.Item item) {
        return TourApi.builder()
                .title(item.getTitle())
                .contentid(item.getContentid())
                .tel(item.getTel())
                .addr1(item.getAddr1())
                .addr2(item.getAddr2())
                .sigungucode(item.getSigungucode())
                .firstimage(item.getFirstimage())
                .firstimage2(item.getFirstimage2())
                .mapx(item.getMapx())
                .mapy(item.getMapy())
                .zipcode(item.getZipcode())
                .createdtime(item.getCreatedtime())
                .modifiedtime(item.getModifiedtime())
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .contenttypeid(item.getContenttypeid())
                .homepage(item.getHomepage())
                .overview(item.getOverview())
                .disabled(item.getDisabled())
                .build();
    }

    private synchronized void saveOrUpdateTour(TourApi tourApi) {
        Optional<TourApi> existingTour = tourApiRepository.findByContentid(tourApi.getContentid());
        if (existingTour.isPresent()) {
            TourApi existing = existingTour.get();
            existing.setTitle(tourApi.getTitle());
            existing.setTel(tourApi.getTel());
            existing.setAddr1(tourApi.getAddr1());
            existing.setAddr2(tourApi.getAddr2());
            existing.setSigungucode(tourApi.getSigungucode());
            existing.setFirstimage(tourApi.getFirstimage());
            existing.setFirstimage2(tourApi.getFirstimage2());
            existing.setMapx(tourApi.getMapx());
            existing.setMapy(tourApi.getMapy());
            existing.setZipcode(tourApi.getZipcode());
            existing.setCreatedtime(tourApi.getCreatedtime());
            existing.setModifiedtime(tourApi.getModifiedtime());
            existing.setCat1(tourApi.getCat1());
            existing.setCat2(tourApi.getCat2());
            existing.setCat3(tourApi.getCat3());
            existing.setContenttypeid(tourApi.getContenttypeid());
            existing.setHomepage(tourApi.getHomepage());
            existing.setOverview(tourApi.getOverview());
            existing.setDisabled(tourApi.getDisabled());
            tourApiRepository.save(existing);
        } else {
            tourApiRepository.save(tourApi);
        }
    }
}
