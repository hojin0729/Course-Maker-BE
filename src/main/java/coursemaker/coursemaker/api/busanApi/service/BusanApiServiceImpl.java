package coursemaker.coursemaker.api.busanApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.busanApi.entity.BusanApi;
import coursemaker.coursemaker.api.busanApi.repository.BusanApiRepository;
import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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

    private final DestinationService destinationService;
    private final TagService tagService;
    private final MemberRepository memberRepository;

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
    public void busanConvertAndSaveToDestination() {
        List<BusanApi> busanApis = busanApiRepository.findAll();
        for (BusanApi busanApi : busanApis) {
            // Destination 테이블에 이미 해당 BusanApi의 seq가 있는지 확인
            Optional<Destination> existingDestination = destinationRepository.findBySeq(busanApi.getSeq());
            if (existingDestination.isEmpty()) {
                // Destination 테이블에 해당 항목이 없으면 새로 저장
                RequestDto dto = new RequestDto();
                LocationDto locationDto = new LocationDto(busanApi.getStartAddr(), null, null);
                Optional<Member> adminMember = memberRepository.findById(1L);
                List<TagResponseDto> tags = tagService.findAllTags();
                // 태그 찾기

                // 자연
                TagResponseDto mountainTag = tags.stream().filter(tag -> "산".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "산 태그"));
                TagResponseDto seaTag = tags.stream().filter(tag -> "바다".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "바다 태그"));
                TagResponseDto natureViewTag = tags.stream().filter(tag -> "자연경관".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "자연경관 태그"));
                TagResponseDto nationalParkTag = tags.stream().filter(tag -> "국/공립공원".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "국/공립공원 태그"));
                TagResponseDto trailTag = tags.stream().filter(tag -> "둘레길".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "둘레길 태그"));

                // 동행
                TagResponseDto soloTravelTag = tags.stream().filter(tag -> "나홀로 여행".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "나홀로 여행 태그"));
                TagResponseDto withKidsTag = tags.stream().filter(tag -> "유아동반".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "유아동반 태그"));
                TagResponseDto accessibleTravelTag = tags.stream().filter(tag -> "무장애여행".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "무장애여행 태그"));
                TagResponseDto childrenTravelTag = tags.stream().filter(tag -> "어린이여행".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "어린이여행 태그"));
                TagResponseDto parentsTravelTag = tags.stream().filter(tag -> "부모님".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "부모님 태그"));
                TagResponseDto coupleTravelTag = tags.stream().filter(tag -> "연인/배우자".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "연인/배우자 태그"));
                TagResponseDto friendsTravelTag = tags.stream().filter(tag -> "친구".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "친구 태그"));
                TagResponseDto withPetsTag = tags.stream().filter(tag -> "애견동반".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "애견동반 태그"));


                // 활동
                TagResponseDto activityTag = tags.stream().filter(tag -> "액티비티".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "액티비티 태그"));
                TagResponseDto cultureMuseumTag = tags.stream().filter(tag -> "문화/박물관".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "문화/박물관 태그"));
                TagResponseDto festivalPerformanceTag = tags.stream().filter(tag -> "축제/공연".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "축제/공연 태그"));
                TagResponseDto experienceTravelTag = tags.stream().filter(tag -> "체험여행".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "체험여행 태그"));
                TagResponseDto traditionalMarketTag = tags.stream().filter(tag -> "전통시장".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "전통시장 태그"));
                TagResponseDto historicalSiteTag = tags.stream().filter(tag -> "역사/유적지".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "역사/유적지 태그"));
                TagResponseDto restFacilityTag = tags.stream().filter(tag -> "휴식시설".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "휴식시설 태그"));
                TagResponseDto foodTravelTag = tags.stream().filter(tag -> "식도락".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "식도락 태그"));

                // 날씨
                TagResponseDto operatingInRainTag = tags.stream().filter(tag -> "우천시 운영".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "우천시 운영 태그"));
                TagResponseDto indoorSpaceTag = tags.stream().filter(tag -> "실내공간".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "실내공간 태그"));
                TagResponseDto vacationSpotTag = tags.stream().filter(tag -> "피서지".equals(tag.getName())).findFirst()
                        .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "피서지 태그"));

                dto.setName(busanApi.getGuganNm());
                dto.setContent(busanApi.getGmCourse());
                dto.setLocation(locationDto);
                dto.setSeq(busanApi.getSeq());
                dto.setApiData(1);
                dto.setAverageRating(0d);
                dto.setNickname(adminMember.get().getNickname());
                dto.setTags(List.of(trailTag));
                destinationService.save(dto);
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