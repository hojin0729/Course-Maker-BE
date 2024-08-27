package coursemaker.coursemaker.domain.like.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.like.dto.CourseLikeRequestDto;
import coursemaker.coursemaker.domain.like.dto.CourseLikeResponseDto;
import coursemaker.coursemaker.domain.like.exception.LikeForbiddenException;
import coursemaker.coursemaker.domain.like.exception.LikeUnauthorizedException;
import coursemaker.coursemaker.domain.like.service.CourseLikeService;
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
@RequestMapping("/v1/courseLikes")
@Tag(name = "CourseLike", description = "코스 좋아요 API")
public class CourseLikeController {

    private CourseLikeService courseLikeService;

    public CourseLikeController(CourseLikeService courseLikeService) {
        this.courseLikeService = courseLikeService;
    }

    /**
     * 코스좋아요 등록
     * */
    @Operation(summary = "코스좋아요 등록", description = "코스 좋아요 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "코스 좋아요가 성공적으로 등록되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid Like\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "이미 좋아요한 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated Like\", \"message\": \"이미 좋아요한 코스입니다.\"}"
                    )
            ))
    })
    @PostMapping
    public ResponseEntity<CourseLikeResponseDto> addCourseLike(@RequestBody CourseLikeRequestDto requestDto,
                                                               @AuthenticationPrincipal LoginedInfo logined) {


        if (logined == null) {
            throw new LikeUnauthorizedException("사용자가 이 자원에 접근할 권한이 없습니다.", "Unauthorized");
        }

        // 요청 DTO에 로그인된 사용자의 닉네임 설정
        requestDto.setNickname(logined.getNickname());

        // 서비스 호출을 통해 코스 좋아요 등록
        CourseLikeResponseDto responseDto = courseLikeService.addCourseLike(requestDto);

        return ResponseEntity.ok(responseDto);
    }



    /**
     *  코스좋아요 취소
     *  */
    @DeleteMapping("/{courseId}")
    @Operation(summary = "코스좋아요 취소", description = "등록한 코스좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "코스 좋아요가 성공적으로 취소되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 코스좋아요를 취소할 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"다른 사용자의 코스좋아요를 취소할 수 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid Like\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<Void> cancelCourseLike(
            @PathVariable Long courseId,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new LikeUnauthorizedException("사용자가 이 자원에 접근할 권한이 없습니다.", "Unauthorized");
        }

        // 현재 로그인된 사용자의 닉네임을 가져와서 서비스에 전달
        courseLikeService.cancelCourseLike(courseId, logined.getNickname());
        return ResponseEntity.noContent().build();
    }


    /**
     *  코스좋아요 닉네임으로 조회
     *  */
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 코스좋아요 조회", description = "닉네임을 사용하여 코스좋아요 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 좋아요가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 코스좋아요를 조회할 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"다른 사용자의 코스좋아요를 조회할 수 없습니다.\"}"
                    )
            ))
    })
    public ResponseEntity<List<CourseLikeResponseDto>> getCourseLikesByNickname(@PathVariable("nickname") String nickname,
                                                                                 @AuthenticationPrincipal LoginedInfo logined) {
        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new LikeUnauthorizedException("사용자가 이 자원에 접근할 권한이 없습니다.", "Unauthorized");
        }

        // 로그인된 사용자의 닉네임과 요청된 닉네임이 일치하는지 확인
        if (!nickname.equals(logined.getNickname())) {
            throw new LikeForbiddenException("다른 사용자의 코스좋아요를 조회할 수 없습니다.", "Forbidden");
        }



        // 서비스 호출을 통해 코스좋아요 목록 조회
        List<CourseLikeResponseDto> responseDtos = courseLikeService.getCourseLikesByNickname(nickname);
        return ResponseEntity.ok(responseDtos);
    }



    /**
     *  코스좋아요 전체조회
     *  */
    @GetMapping
    @Operation(summary = "코스좋아요 목록 전체조회", description = "코스좋아요 목록을 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 좋아요이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "코스 좋아요이 존재하지 않습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid Like\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<List<CourseLikeResponseDto>> getAllCourseLikes() {
        // 서비스 호출을 통해 전체 코스좋아요 목록 조회
        List<CourseLikeResponseDto> responseDtos = courseLikeService.getAllCourseLikes();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 코스별 좋아요된 수 조회
     */
    @GetMapping("/count/{courseId}")
    @Operation(summary = "코스별 좋아요된 수 조회", description = "특정 코스에 좋아요된 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 좋아요 수가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 코스입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid Like\", \"message\": \"존재하지 않는 코스입니다.\"}"
                    )
            ))
    })
    public ResponseEntity<Integer> getCourseLikeCount(@PathVariable("courseId") Long courseId) {

            Integer LikeCount = courseLikeService.getCourseLikeCount(courseId);

            return ResponseEntity.ok(LikeCount);

    }

}
