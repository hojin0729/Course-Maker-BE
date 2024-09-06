package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.auth.exception.LoginRequiredException;
import coursemaker.coursemaker.domain.wish.dto.CourseWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.exception.WishForbiddenException;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * 코스찜 등록
     * */
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
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "이미 찜한 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated wish\", \"message\": \"이미 찜한 코스입니다.\"}"
                    )
            ))
    })
    @PostMapping
    public ResponseEntity<CourseWishResponseDto> addCourseWish(@RequestBody CourseWishRequestDto requestDto,
                                                               @AuthenticationPrincipal LoginedInfo logined) {


        if (logined == null) {
            throw new LoginRequiredException("로그인 후 이용이 가능합니다.", "[CourseWish] addCourseWish");
        }

        // 요청 DTO에 로그인된 사용자의 닉네임 설정
        requestDto.setNickname(logined.getNickname());

        // 서비스 호출을 통해 코스 찜 등록
        CourseWishResponseDto responseDto = courseWishService.addCourseWish(requestDto);

        return ResponseEntity.ok(responseDto);
    }



    /**
     *  코스찜 취소
     *  */
    @DeleteMapping("/{courseId}")
    @Operation(summary = "코스찜 취소", description = "등록한 코스찜을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "코스 찜이 성공적으로 취소되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 코스찜을 취소할 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"다른 사용자의 코스찜을 취소할 수 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<Void> cancelCourseWish(@PathVariable("courseId") Long courseId,
                                                 @AuthenticationPrincipal LoginedInfo logined
                                                 ) {

        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new LoginRequiredException("로그인 후 이용이 가능합니다.", "[CourseWish] cancelCourseWish");
        }

        // 현재 로그인된 사용자의 닉네임을 가져와서 서비스에 전달
        courseWishService.cancelCourseWish(courseId, logined.getNickname());
        return ResponseEntity.noContent().build();
    }


    /**
     *  코스찜 닉네임으로 조회
     *  */
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 코스찜 조회", description = "닉네임을 사용하여 코스찜 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 찜이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 코스찜을 조회할 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"다른 사용자의 코스찜을 조회할 수 없습니다.\"}"
                    )
            ))
    })
    public ResponseEntity<List<CourseWishResponseDto>> getCourseWishesByNickname(@PathVariable("nickname") String nickname,
                                                                                 @AuthenticationPrincipal LoginedInfo logined) {
        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new LoginRequiredException("로그인 후 이용이 가능합니다.", "[CourseWish] getCourseWishesByNickname");
        }

        // 로그인된 사용자의 닉네임과 요청된 닉네임이 일치하는지 확인
        if (!nickname.equals(logined.getNickname())) {
            throw new WishForbiddenException("다른 사용자의 코스찜을 조회할 수 없습니다.", "Forbidden");
        }



        // 서비스 호출을 통해 코스찜 목록 조회
        List<CourseWishResponseDto> responseDtos = courseWishService.getCourseWishesByNickname(nickname);
        return ResponseEntity.ok(responseDtos);
    }



    /**
     *  코스찜 전체조회
     *  */
    @GetMapping
    @Operation(summary = "코스찜 목록 전체조회", description = "코스찜 목록을 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 찜이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "코스 찜이 존재하지 않습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<List<CourseWishResponseDto>> getAllCourseWishes() {
        // 서비스 호출을 통해 전체 코스찜 목록 조회
        List<CourseWishResponseDto> responseDtos = courseWishService.getAllCourseWishes();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 코스별 찜된 수 조회
     */
    @GetMapping("/count/{courseId}")
    @Operation(summary = "코스별 찜된 수 조회", description = "특정 코스에 찜된 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 찜 수가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<Integer> getCourseWishCount(@PathVariable("courseId") Long courseId) {

            Integer wishCount = courseWishService.getCourseWishCount(courseId);

            return ResponseEntity.ok(wishCount);

    }

}
