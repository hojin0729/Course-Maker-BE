package coursemaker.coursemaker.api.tourApi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class TourApiService {

    @Value("${openapi.key}")
    private String secretKey;

    public String getByArea() {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).build();

        String response = webClient
                .get()
                .uri(uriBuilder -> {
                    try {
                        return uriBuilder
                                .scheme("http")
                                .host("apis.data.go.kr")
                                .path("/B551011/KorService1")
                                .path("/areaCode1")
                                .queryParam("MobileOS", "ETC")
                                .queryParam("MobileApp", "AppTest")
                                .queryParam("numOfRows", "10")
                                .queryParam("pageNo", "1")
                                .queryParam("_type", "json")
                                .queryParam("serviceKey", URLEncoder.encode(secretKey, "UTF-8"))
                                .build();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
