package coursemaker.coursemaker.api.tourApi;

import coursemaker.coursemaker.api.tourApi.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tour-api-test")
@RequiredArgsConstructor
public class TourApiTestController {

    private final TourApiService tourApiService;

    @GetMapping
    public String test(Model model) {
        String response = tourApiService.getByArea();
        model.addAttribute("response", response);
        return "tour-api-test";
    }
}
