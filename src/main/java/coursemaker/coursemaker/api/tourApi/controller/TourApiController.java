package coursemaker.coursemaker.api.tourApi.controller;

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

//    @GetMapping("/updateDisabledTours")
//    public void updateDisabledTours() {
//        tourApiService.updateDisabledTours();
//    }

//    @GetMapping("/updateCommonData1")
//    public void updateCommonData1() {
//        tourApiService.updateTourWithCommonData1();
//    }
//    @GetMapping("/updateCommonData2")
//    public void updateCommonData2() {
//        tourApiService.updateTourWithCommonData2();
//    }
//    @GetMapping("/updateCommonData3")
//    public void updateCommonData3() {
//        tourApiService.updateTourWithCommonData3();
//    }
//    @GetMapping("/updateCommonData4")
//    public void updateCommonData4() {
//        tourApiService.updateTourWithCommonData4();
//    }
//    @GetMapping("/updateCommonData5")
//    public void updateCommonData5() {
//        tourApiService.updateTourWithCommonData5();
//    }
//    @GetMapping("/updateCommonData6")
//    public void updateCommonData6() {
//        tourApiService.updateTourWithCommonData6();
//    }
//    @GetMapping("/updateCommonData7")
//    public void updateCommonData7() {
//        tourApiService.updateTourWithCommonData7();
//    }
//    @GetMapping("/updateCommonData8")
//    public void updateCommonData8() {
//        tourApiService.updateTourWithCommonData8();
//    }
//    @GetMapping("/updateCommonData9")
//    public void updateCommonData9() {
//        tourApiService.updateTourWithCommonData9();
//    }
//    @GetMapping("/updateCommonData10")
//    public void updateCommonData10() {
//        tourApiService.updateTourWithCommonData10();
//    }



//    @GetMapping("/updateIntroData1")
//    public void updateIntroData1() {
//        tourApiService.updateTourWithIntroData1();
//    }
//    @GetMapping("/updateIntroData2")
//    public void updateIntroData2() {
//        tourApiService.updateTourWithIntroData2();
//    }
//    @GetMapping("/updateIntroData3")
//    public void updateIntroData3() {
//        tourApiService.updateTourWithIntroData3();
//    }
//    @GetMapping("/updateIntroData4")
//    public void updateIntroData4() {
//        tourApiService.updateTourWithIntroData4();
//    }
//    @GetMapping("/updateIntroData5")
//    public void updateIntroData5() {
//        tourApiService.updateTourWithIntroData5();
//    }
//    @GetMapping("/updateIntroData6")
//    public void updateIntroData6() {
//        tourApiService.updateTourWithIntroData6();
//    }
//    @GetMapping("/updateIntroData7")
//    public void updateIntroData7() {
//        tourApiService.updateTourWithIntroData7();
//    }
//    @GetMapping("/updateIntroData8")
//    public void updateIntroData8() {
//        tourApiService.updateTourWithIntroData8();
//    }
//    @GetMapping("/updateIntroData9")
//    public void updateIntroData9() {
//        tourApiService.updateTourWithIntroData9();
//    }
//    @GetMapping("/updateIntroData10")
//    public void updateIntroData10() {
//        tourApiService.updateTourWithIntroData10();
//    }

    // 호진님이 작업하셨었던 코드
//    @GetMapping
//    public String test(Model model) {
//        String response = tourApiService.getByArea();
//        model.addAttribute("response", response);
//        return "tour-api-test";
//    }
//
//    @GetMapping
//    public String test2(Model model) {
//        AreaTourResponse response = tourApiService.getAreaTourList();
//        model.addAttribute("response", response);
//        return "tour-api-test";
//    }
}

