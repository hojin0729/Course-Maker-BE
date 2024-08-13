package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.review.dto.ResponseCourseDto;
import coursemaker.coursemaker.domain.wish.dto.CourseWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(summary = "코스찜 등록", description = "코스 찜 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "코스 찜이 성공적으로 등록되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    @PostMapping
    public ResponseEntity<CourseWishResponseDto> addCourseWish(@RequestBody CourseWishRequestDto requestDto,
                                                               @AuthenticationPrincipal LoginedInfo logined) {

        String nickname = logined.getNickname();
        requestDto.setNickname(nickname);
        CourseWish courseWish = courseWishService.addCourseWish(requestDto.getCourseId(), requestDto.getNickname());
        CourseWishResponseDto responseDto = new CourseWishResponseDto(
                courseWish.getId(),
                courseWish.getTravelCourse().getId(),
                courseWish.getTravelCourse().getTitle(),
                courseWish.getMember().getNickname()
        );
        return ResponseEntity.ok(responseDto);
    }



    /*코스찜 취소*/
    @Operation(summary = "코스찜 취소", description = "등록한 코스찜을 취소합니다.")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> cancelCourseWish(
            @PathVariable Long courseId,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 인증된 사용자와 요청된 memberId가 일치하는지 확인.
        String nickname = logined.getNickname();

        courseWishService.cancelCourseWish(courseId, nickname);
        return ResponseEntity.noContent().build();
    }


    /*코스찜 닉네임으로 조회*/
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 코스찜 조회", description = "넥네임을 사용하여 코스찜을 목록 조회합니다.")
    @Parameter(name = "nickname", description = "코스찜한 사용자의 nickname", required = true)
    public ResponseEntity<List<CourseWishResponseDto>> getCourseWishesByNickname(@PathVariable String nickname,
                                                                                 @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인된 사용자의 닉네임과 요청된 닉네임이 일치하는지 확인
        if (!nickname.equals(logined.getNickname())) {
            throw new RuntimeException("요청된 닉네임이 로그인된 사용자와 일치하지 않습니다.");
        }

        List<CourseWish> courseWishes = courseWishService.getCourseWishesByNickname(nickname);
        List<CourseWishResponseDto> responseDtos = courseWishes.stream()
                .map(courseWish -> new CourseWishResponseDto(
                        courseWish.getId(),
                        courseWish.getTravelCourse().getId(),
                        courseWish.getTravelCourse().getTitle(),
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
                        courseWish.getMember().getNickname()
                ))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

}
