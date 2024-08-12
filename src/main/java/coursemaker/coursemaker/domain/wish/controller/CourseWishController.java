package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/coursewishes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "CourseWish", description = "코스 찜 API")
public class CourseWishController {

    private CourseWishService courseWishService;
    private CourseWish courseWish;

    /*코스찜 등록*/
    @PostMapping
    public ResponseEntity<CourseWish> addCourseWish(@RequestParam Long courseId, @RequestParam Long memberId) {
        CourseWish courseWish = courseWishService.addCourseWish(courseId, memberId);
        return ResponseEntity.ok(courseWish);
    }



    /*코스찜 취소*/
    @DeleteMapping
    public ResponseEntity<Void> cancelCourseWish(@RequestParam Long courseId, @RequestParam Long memberId) {
        courseWishService.cancelCourseWish(courseId, memberId);
        return ResponseEntity.noContent().build();
    }

    /*코스찜 닉네임으로 조회*/
    @Operation(summary = "닉네임으로 코스찜 목록 조회")
    @GetMapping("/{nickname}")
    public ResponseEntity<List<CourseWish>> getCourseWishesByNickname(@RequestParam String nickname) {
        List<CourseWish> courseWishes = courseWishService.getCourseWishesByNickname(nickname);
        return ResponseEntity.ok(courseWishes);
    }

    /*코스찜 전체조회*/
    @Operation(summary = "코스찜 목록 전체조회")
    @GetMapping
    public ResponseEntity<List<CourseWish>> getAllCourseWishes() {
        List<CourseWish> courseWishes = courseWishService.getAllCourseWishes();
        return ResponseEntity.ok(courseWishes);
    }

}
