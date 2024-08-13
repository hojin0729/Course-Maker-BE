package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.wish.dto.CourseWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/coursewishes")
@Tag(name = "CourseWish", description = "코스 찜 API")
public class CourseWishController {

    private CourseWishService courseWishService;

    public CourseWishController(CourseWishService courseWishService) {
        this.courseWishService = courseWishService;
    }

    /*코스찜 등록*/
    @PostMapping
    @Operation(summary = "코스찜 등록", description = "코스 찜 등록합니다.")
    public ResponseEntity<CourseWishResponseDto> addCourseWish(@RequestBody CourseWishRequestDto requestDto) {
        CourseWish courseWish = courseWishService.addCourseWish(requestDto.getCourseId(), requestDto.getMemberId());
        CourseWishResponseDto responseDto = new CourseWishResponseDto(
                courseWish.getId(),
                courseWish.getTravelCourse().getId(),
                courseWish.getTravelCourse().getTitle(),
                courseWish.getMember().getId(),
                courseWish.getMember().getNickname()
        );
        return ResponseEntity.ok(responseDto);
    }



    /*코스찜 취소*/
    @DeleteMapping("/{courseId}/{memberId}")
    @Operation(summary = "코스찜 취소", description = "등록한 코스찜을 취소합니다.")
    public ResponseEntity<Void> cancelCourseWish(
            @PathVariable Long courseId,
            @PathVariable Long memberId) {
        courseWishService.cancelCourseWish(courseId, memberId);
        return ResponseEntity.noContent().build();
    }

    /*코스찜 닉네임으로 조회*/
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 코스찜 조회", description = "넥네임을 사용하여 코스찜을 목록 조회합니다.")
    @Parameter(name = "nickname", description = "코스찜한 사용자의 nickname", required = true)
    public ResponseEntity<List<CourseWishResponseDto>> getCourseWishesByNickname(@PathVariable String nickname) {
        List<CourseWish> courseWishes = courseWishService.getCourseWishesByNickname(nickname);
        List<CourseWishResponseDto> responseDtos = courseWishes.stream()
                .map(courseWish -> new CourseWishResponseDto(
                        courseWish.getId(),
                        courseWish.getTravelCourse().getId(),
                        courseWish.getTravelCourse().getTitle(),
                        courseWish.getMember().getId(),
                        courseWish.getMember().getNickname()
                ))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    /*코스찜 전체조회*/
    @GetMapping
    @Operation(summary = "코스찜 목록 전체조회", description = "코스찜 목록을 전체조회합니다.")
    public ResponseEntity<List<CourseWishResponseDto>> getAllCourseWishes() {
        List<CourseWish> courseWishes = courseWishService.getAllCourseWishes();
        List<CourseWishResponseDto> responseDtos = courseWishes.stream()
                .map(courseWish -> new CourseWishResponseDto(
                        courseWish.getId(),
                        courseWish.getTravelCourse().getId(),
                        courseWish.getTravelCourse().getTitle(),
                        courseWish.getMember().getId(),
                        courseWish.getMember().getNickname()
                ))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

}
