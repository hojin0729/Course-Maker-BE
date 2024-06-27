package coursemaker.coursemaker.api.tourApi.service;

import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.repository.TourApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    @Value("${tourapi.serviceKey}")
    private String serviceKey;

    @Value("${tourapi.baseUrl}")
    private String baseUrl;


    private final RestTemplate restTemplate;
    private final TourApiRepository tourApiRepository;

    @Override
    public TourApiResponse getTour() {

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("numOfRows", 3000)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("areaCode", 6)
                .queryParam("serviceKey", serviceKey)
                .build(true) // 인코딩을 자동으로 처리
                .toUri();

        // API 요청 및 응답 처리
        TourApiResponse response = restTemplate.getForObject(uri, TourApiResponse.class);

        if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
            List<TourApi> tourList = response.getResponse().getBody().getItems().getItem().stream()
                    .map(this::convertToEntity)
                    .toList();
            tourList.forEach(this::saveOrUpdateTour);
        }

        return response;
    }

//    @Override
//    public List<Tour> getAllTours() {
//        return tourRepository.findAll();
//    }


    @Override
    public Optional<TourApi> getTourById(Long id) {
        return tourApiRepository.findById(id);
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
                .build();
    }

    private void saveOrUpdateTour(TourApi tourApi) {
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
            tourApiRepository.save(existing);
        } else {
            tourApiRepository.save(tourApi);
        }
    }
}

