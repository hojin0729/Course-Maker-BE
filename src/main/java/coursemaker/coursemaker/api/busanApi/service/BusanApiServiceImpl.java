package coursemaker.coursemaker.api.busanApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.busanApi.entity.BusanApi;
import coursemaker.coursemaker.api.busanApi.repository.BusanApiRepository;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusanApiServiceImpl implements BusanApiService {
    @Value("${busanapi.serviceKey}")
    private String serviceKey;

    @Value("${busanapi.baseUrl}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;
    private final BusanApiRepository busanApiRepository;
    private final DestinationRepository destinationRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public BusanApiResponse updateAndGetTour() {
        CompletableFuture<BusanApiResponse> initialUpdateFuture = CompletableFuture.supplyAsync(this::initialUpdate, executorService);

        initialUpdateFuture
                .thenRunAsync(this::busanConvertAndSaveToDestination, executorService)
                .join();

        return initialUpdateFuture.join();
    }

    @Override
    public BusanApiResponse initialUpdate() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("numOfRows", 30)
                .queryParam("pageNo", 1)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();
        try {
            String responseBody = webClientBuilder.build().get()
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
                    .bodyToMono(String.class)  // 일단 String으로 응답 받기
                    .doOnNext(res -> log.debug("Received raw response: {}", res))
                    .block();

            // Raw response를 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            BusanApiResponse response = objectMapper.readValue(responseBody, BusanApiResponse.class);

            if (response != null && response.getResponse() != null && response.getResponse().getBody() != null && response.getResponse().getBody().getItems().getItem() != null) {
                List<BusanApi> tourList = response.getResponse().getBody().getItems().getItem().stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());
                synchronized (this) {
                    tourList.forEach(this::saveOrUpdateTour);
                }
            } else {
                log.error("Received null or incomplete response");
            }
            return response;
        } catch (Exception e) {
            log.error("Exception occurred while updating tours: ", e);
            throw new RuntimeException("Failed to update tours", e);
        }
    }

    @Override
    public List<BusanApi> getAllTours() {
        return busanApiRepository.findAll();
    }

    @Override
    public Optional<BusanApi> getTourById(Long id) {
        return busanApiRepository.findById(id);
    }

    @Override
    public void busanConvertAndSaveToDestination() {
        List<BusanApi> busanApis = busanApiRepository.findAll();
        for (BusanApi busanApi : busanApis) {
            // Destination 테이블에 이미 해당 BusanApi의 seq가 있는지 확인
            Optional<Destination> existingDestination = destinationRepository.findBySeq(busanApi.getSeq());
            if (existingDestination.isEmpty()) {
                // Destination 테이블에 해당 항목이 없으면 새로 저장
                Destination destination = new Destination();
                destination.setName(busanApi.getGuganNm());
                destination.setViews(0);
                destination.setContent(busanApi.getGmCourse());
                destination.setLocation(busanApi.getStartAddr());
                destination.setSeq(busanApi.getSeq());
                destination.setApiData(1);
                destination.setAverageRating(0d);

                // createdAt과 updatedAt은 String에서 LocalDateTime으로 변환하여 설정
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//                LocalDateTime createdAt = LocalDateTime.parse(busanApi.getCreatedtime(), formatter);
//                LocalDateTime updatedAt = LocalDateTime.parse(busanApi.getModifiedtime(), formatter);
//                destination.setCreatedAt(createdAt);
//                destination.setUpdatedAt(updatedAt);

                destinationRepository.save(destination);
            }
        }
    }

    private BusanApi convertToEntity(BusanApiResponse.Item item) {
        return BusanApi.builder()
                .guganNm(item.getGuganNm())
                .courseNm(item.getCourseNm())
                .seq(item.getSeq())
                .gmRange(item.getGmRange())
                .gmDegree(item.getGmDegree())
                .startPls(item.getStartPls())
                .startAddr(item.getStartAddr())
                .middlePls(item.getMiddlePls())
                .middleAdr(item.getMiddleAdr())
                .endPls(item.getEndPls())
                .endAddr(item.getEndAddr())
                .gmCourse(item.getGmCourse())
                .gmText(item.getGmText())
                .build();
    }

    private synchronized void saveOrUpdateTour(BusanApi busanApi) {
        Optional<BusanApi> existingTour = busanApiRepository.findBySeq(busanApi.getSeq());
        if (existingTour.isPresent()) {
            BusanApi existing = existingTour.get();
            existing.setGuganNm(busanApi.getGuganNm());
            existing.setCourseNm(busanApi.getCourseNm());
            existing.setSeq(busanApi.getSeq());
            existing.setGmRange(busanApi.getGmRange());
            existing.setGmDegree(busanApi.getGmDegree());
            existing.setStartPls(busanApi.getStartPls());
            existing.setStartAddr(busanApi.getStartAddr());
            existing.setMiddlePls(busanApi.getMiddlePls());
            existing.setMiddleAdr(busanApi.getMiddleAdr());
            existing.setEndPls(busanApi.getEndPls());
            existing.setEndAddr(busanApi.getEndAddr());
            existing.setGmCourse(busanApi.getGmCourse());
            existing.setGmText(busanApi.getGmText());
            busanApiRepository.save(existing);
        } else {
            busanApiRepository.save(busanApi);
        }
    }
}