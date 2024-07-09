package coursemaker.coursemaker.api.tourApi.service;

import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;

import java.util.List;
import java.util.Optional;

public interface TourApiService {

    // TagResponseDto findTagByName(String name);
    TourApiResponse updateAndGetTour();
    Optional<TourApi> getTourById(Long id);
    List<TourApi> getAllTours();
    void updateDisabledTours();
//    void updateTourWithCommonData1();
//    void updateTourWithCommonData2();
//    void updateTourWithCommonData3();
//    void updateTourWithCommonData4();
//    void updateTourWithCommonData5();
//    void updateTourWithCommonData6();
//    void updateTourWithCommonData7();
//    void updateTourWithCommonData8();
//    void updateTourWithCommonData9();
//    void updateTourWithCommonData10();
//    void updateTourWithIntroData1();
//    void updateTourWithIntroData2();
//    void updateTourWithIntroData3();
//    void updateTourWithIntroData4();
//    void updateTourWithIntroData5();
//    void updateTourWithIntroData6();
//    void updateTourWithIntroData7();
//    void updateTourWithIntroData8();
//    void updateTourWithIntroData9();
//    void updateTourWithIntroData10();


    // 호진님이 작업하셨었던 코드

//    @Value("${openapi.key}")
//    private String secretKey;
//
//    public String getByArea() {
//
//        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
//        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
//        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).build();
//
//        String response = webClient
//                .get()
//                .uri(uriBuilder -> {
//                    try {
//                        return uriBuilder
//                                .scheme("http")
//                                .host("apis.data.go.kr")
//                                .path("/B551011/KorService1")
//                                .path("/areaCode1")
//                                .queryParam("MobileOS", "ETC")
//                                .queryParam("MobileApp", "AppTest")
//                                .queryParam("numOfRows", "10")
//                                .queryParam("pageNo", "1")
//                                .queryParam("_type", "json")
//                                .queryParam("serviceKey", URLEncoder.encode(secretKey, "UTF-8"))
//                                .build();
//                    } catch (UnsupportedEncodingException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return response;
//    }
//
//    public AreaTourResponse getAreaTourList() {
//        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
//        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
//        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).build();
//
//        AreaTourResponse response = webClient
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("http")
//                        .host("apis.data.go.kr")
//                        .path("/B551011/KorService1/areaBasedList1")
//                        .queryParam("serviceKey", secretKey)
//                        .queryParam("pageNo", "1")
//                        .queryParam("numOfRows", "10")
//                        .queryParam("MobileApp", "AppTest")
//                        .queryParam("MobileOS", "ETC")
//                        .queryParam("arrange", "A")
//                        .queryParam("contentTypeId", "32")
//                        .queryParam("_type", "json")
//                        .build())
//                .retrieve()
//                .bodyToMono(AreaTourResponse.class)
//                .block();
//
//        return response;
//    }
}