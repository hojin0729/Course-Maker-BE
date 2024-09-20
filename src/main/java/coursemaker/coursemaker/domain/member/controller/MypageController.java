package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.auth.exception.LoginRequiredException;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.member.dto.BasicUserInfoResponseDTO;
import coursemaker.coursemaker.domain.member.service.MypageService;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/my")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Mypage", description = "마이페이지 관련 API")
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "기본 정보 조회", description = "로그인 한 사용자의 기본 정보인 이름, 닉네임, 등급, 프로필 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기본정보 조회 성공", content = @Content(schema = @Schema(implementation = BasicUserInfoResponseDTO.class))),

            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            ))
    })
    @GetMapping("/basicInfo")
    public ResponseEntity<BasicUserInfoResponseDTO> getBasicInfo(@AuthenticationPrincipal LoginedInfo loginedInfo) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        BasicUserInfoResponseDTO response = mypageService.getBasicUserInfo(loginedInfo.getNickname());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 만든 여행 코스 조회", description = "로그인 한 사용자가 만든 코스를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 코스를 찾을 수 없습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/course")
    public ResponseEntity<CourseMakerPagination<TravelCourseResponse>> getMyCourse(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                                   @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                   @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<TravelCourseResponse> response = mypageService.getMyCourses(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "내가 찜 한 여행 코스 조회", description = "로그인 한 사용자가 찜 한 코스를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "찜 한 코스가 존재하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"코스 찜이 존재하지 않습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/wish/course")
    public ResponseEntity<CourseMakerPagination<TravelCourseResponse>> getMyWishCourse(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                                   @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                   @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<TravelCourseResponse> response = mypageService.getMyWishCourse(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 좋아요 한 여행 코스 조회", description = "로그인 한 사용자가 좋아요 한 코스를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "좋아요 한 코스가 존재하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid like\", \"message\": \"코스 좋아요가 존재하지 않습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/like/course")
    public ResponseEntity<CourseMakerPagination<TravelCourseResponse>> getMyLikeCourse(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                                       @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                       @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<TravelCourseResponse> response = mypageService.getMyLikeCourse(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "내가 만든 여행 여행지 조회", description = "로그인 한 사용자가 만든 여행지를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 여행지를 찾을 수 없습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/destination")
    public ResponseEntity<CourseMakerPagination<DestinationDto>> getMyDestination(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                             @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                             @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<DestinationDto> response = mypageService.getMyDestination(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "내가 찜 한 여행 여행지 조회", description = "로그인 한 사용자가 찜 한 여행자를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "찜 한 코스가 존재하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid wish\", \"message\": \"여행지 찜이 존재하지 않습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/wish/destination")
    public ResponseEntity<CourseMakerPagination<DestinationDto>> getMyWishDestination(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                                       @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                       @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<DestinationDto> response = mypageService.getMyWishDestination(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "내가 좋아요 한 여행 여행지 조회", description = "로그인 한 사용자가 좋아요 한 여행지를 정렬 기준에 맞게 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "좋아요 한 여행지가 존재하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid like\", \"message\": \"여행지 좋아요가 존재하지 않습니다.\"}"
                    )
            ))
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수"),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)"),
    })
    @GetMapping("/like/destination")
    public ResponseEntity<CourseMakerPagination<DestinationDto>> getMyLikeDestination(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                                                                       @RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                       @RequestParam(defaultValue = "1", name = "page") Integer page
    ) {
        if(loginedInfo == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[MEMBER] 비 로그인 사용자 마이페이지 접근");
        }

        CourseMakerPagination<DestinationDto> response = mypageService.getMyLikeDestination(loginedInfo.getNickname(), PageRequest.of(page - 1, record));

        return ResponseEntity.ok(response);
    }



}
