package coursemaker.coursemaker.api.busanApi.controller;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.busanApi.entity.BusanApi;
import coursemaker.coursemaker.api.busanApi.service.BusanApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/busanTours/")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "TourApi", description = "백엔드에서만 내부적으로 사용하는 API 입니다.")
public class BusanApiController {

    private final BusanApiService tourApiService;


    @GetMapping("/update")
    public BusanApiResponse updateAndGetTourData() {
        return tourApiService.updateAndGetTour();
    }

    @GetMapping("/{id}")
    public Optional<BusanApi> getTourById(@PathVariable Long id) {
        return tourApiService.getTourById(id);
    }

    @GetMapping
    public List<BusanApi> getTours() {
        return tourApiService.getAllTours();
    }
}