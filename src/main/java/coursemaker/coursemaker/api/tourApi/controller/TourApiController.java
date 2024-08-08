package coursemaker.coursemaker.api.tourApi.controller;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/tours")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "TourApi", description = "백엔드에서만 내부적으로 사용하는 API 입니다.")
public class TourApiController {

    private final TourApiService tourApiService;


    @GetMapping("/update")
    public TourApiResponse updateAndGetTourData() {
        return tourApiService.updateAndGetTour();
    }

    @GetMapping("/{id}")
    public Optional<TourApi> getTourById(@PathVariable Long id) {
        return tourApiService.getTourById(id);
    }

    @GetMapping
    public List<TourApi> getTours() {
        return tourApiService.getAllTours();
    }
}