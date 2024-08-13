package coursemaker.coursemaker.api.tourApi.controller;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.service.TourApiService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    /*********스웨거 어노테이션**********/
    @Operation(summary = "공공데이터 불러오기 및 업데이트", description = "실행하면 tourApi 및 부산광역시 공공데이터를 불러와서 Destination DB에 저장됩니다.")
    /*********스웨거 어노테이션**********/
    @GetMapping("/update")
    public TourApiResponse updateAndGetTourData() {
        return tourApiService.updateAndGetTour();
    }

    /*********스웨거 어노테이션**********/
    @Operation(summary = "공공데이터 id로 찾기", description = "공공데이터를 id로 찾습니다.")
    @Parameters({
            @Parameter(name = "id", description = "공공데이터 고유의 id값 입니다."),
    })
    /*********스웨거 어노테이션**********/
    @GetMapping("/{id}")
    public Optional<TourApi> getTourById(@PathVariable Long id) {
        return tourApiService.getTourById(id);
    }

    /*********스웨거 어노테이션**********/
    @Operation(summary = "모든 공공데이터 조회", description = "모든 공공데이터를 조회합니다.")
    /*********스웨거 어노테이션**********/
    @GetMapping
    public List<TourApi> getTours() {
        return tourApiService.getAllTours();
    }
}
