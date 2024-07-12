package coursemaker.coursemaker.domain.review.controller;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.review.dto.RequestCourseDto;
import coursemaker.coursemaker.domain.review.dto.ResponseCourseDto;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.domain.review.service.CourseReviewService;
import coursemaker.coursemaker.util.LoginUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("v1/coursereview")
public class CourseReviewController {
    CourseReviewService courseReviewService;
    CourseService courseService;


    public CourseReviewController(CourseReviewService courseReviewService, CourseService courseService) {
        this.courseReviewService = courseReviewService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> getCourseReviewById(@PathVariable("id") Long id) {
        CourseReview courseReview = courseReviewService.findById(id);
        TravelCourse travelCourse = courseService.findById(courseReview.getTravelCourse().getId());
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, courseReview);
        return ResponseEntity.ok(responseCourseDto);
    }

    @PostMapping
    public ResponseEntity<ResponseCourseDto> createCourseReview(@RequestBody @Valid RequestCourseDto requestCourseDto,
                                                                @RequestParam(name = "courseId") Long courseId,
                                                                @LoginUser String nickname) {
        requestCourseDto.setNickname(nickname);
        CourseReview savedCourseReview = courseReviewService.save(requestCourseDto, courseId);
        TravelCourse travelCourse = courseService.findById(courseId);
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, savedCourseReview);
        return ResponseEntity.created(URI.create("/v1/coursereview/" + savedCourseReview.getId())).body(responseCourseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> updateCourseReview(@RequestBody @Valid RequestCourseDto requestCourseDto,
                                                                @RequestParam(name = "courseId") Long courseId,
                                                                @LoginUser String nickname) {
        requestCourseDto.setNickname(nickname);
        CourseReview updatedCourseReview = courseReviewService.update(courseId, requestCourseDto, nickname);
        TravelCourse travelCourse = courseService.findById(courseId);
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, updatedCourseReview);
        return ResponseEntity.ok(responseCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCourseReview(@PathVariable("id") Long id, @LoginUser String nickname) {
        // 해당 ID의 리뷰가 존재하는지 확인합니다.
        CourseReview courseReview = courseReviewService.findById(id);
        if (courseReview == null) {
            return ResponseEntity.notFound().build();
        }
        // 해당 리뷰가 로그인한 사용자에게 속하는지 확인합니다.
        if (!courseReview.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("Forbidden", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        // 리뷰를 삭제합니다.
        courseReviewService.delete(id);
        return ResponseEntity.ok(id);
    }
}
