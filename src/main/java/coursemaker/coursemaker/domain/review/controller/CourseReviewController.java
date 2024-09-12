package coursemaker.coursemaker.domain.review.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.auth.exception.LoginRequiredException;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.review.dto.RequestCourseDto;
import coursemaker.coursemaker.domain.review.dto.ResponseCourseDto;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.domain.review.service.CourseReviewService;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@io.swagger.v3.oas.annotations.tags.Tag(name = "CourseReview", description = "코스 리뷰 API")
@RestController
@RequestMapping("v1/coursereview")
public class CourseReviewController {
    private final CourseReviewService courseReviewService;
    private final CourseService courseService;

    public CourseReviewController(CourseReviewService courseReviewService, CourseService courseService) {
        this.courseReviewService = courseReviewService;
        this.courseService = courseService;
    }

    @Operation(summary = "리뷰 조회", description = "리뷰 ID를 입력하여 해당 리뷰의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 ID에 맞는 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID에 맞는 리뷰를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> getCourseReviewById(@PathVariable("id") Long id,
                                                                 @AuthenticationPrincipal LoginedInfo logined) {
        CourseReview courseReview = courseReviewService.findById(id);
        Boolean isMyCourseReview = logined != null && logined.getNickname().equals(courseReview.getMember().getNickname());
        TravelCourse travelCourse = courseService.findById(courseReview.getTravelCourse().getId());
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, courseReview, isMyCourseReview);
        return ResponseEntity.ok(responseCourseDto);
    }

    @Operation(summary = "리뷰 생성", description = "새로운 리뷰 정보를 입력하여 리뷰를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰가 성공적으로 생성되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "생성하려는 리뷰의 인자값이 올바르지 않을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
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
    public ResponseEntity<ResponseCourseDto> createCourseReview(@RequestBody @Valid RequestCourseDto requestCourseDto,
                                                                @RequestParam(name = "courseId") Long courseId,
                                                                @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인한 사용자 닉네임 가져오기
        if(logined==null){
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[CourseReview] 리뷰 생성 실패");
        }
        String nickname = logined.getNickname();
        requestCourseDto.setNickname(nickname);

        CourseReview savedCourseReview = courseReviewService.save(requestCourseDto, courseId);
        // 로그인한 사용자와 리뷰 작성자가 같은지 여부를 확인
        Boolean isMyCourseReview = logined.getNickname().equals(savedCourseReview.getMember().getNickname());
        TravelCourse travelCourse = courseService.findById(courseId);
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, savedCourseReview, isMyCourseReview);
        return ResponseEntity.created(URI.create("/v1/coursereview/" + savedCourseReview.getId())).body(responseCourseDto);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 ID와 수정할 정보를 입력하여 해당 리뷰의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID에 해당하는 리뷰 수정 성공", content = @Content(schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "400", description = "수정하려는 리뷰의 인자값이 올바르지 않을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"접근 권한이 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "수정하려는 리뷰의 ID를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> updateCourseReview(@RequestBody @Valid RequestCourseDto requestCourseDto,
                                                                @RequestParam(name = "courseId") Long courseId,
                                                                @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인한 사용자 닉네임 가져오기

        if(logined==null){
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[CourseReview] 리뷰 생성 실패");
        }

        String nickname = logined.getNickname();
        requestCourseDto.setNickname(nickname);

        CourseReview updatedCourseReview = courseReviewService.update(courseId, requestCourseDto, nickname);
        TravelCourse travelCourse = courseService.findById(courseId);
        Boolean isMyCourseReview = logined.getNickname().equals(updatedCourseReview.getMember().getNickname());
        ResponseCourseDto responseCourseDto = ResponseCourseDto.toDto(travelCourse, updatedCourseReview, isMyCourseReview);
        return ResponseEntity.ok(responseCourseDto);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 ID를 입력하여 해당 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID에 해당하는 리뷰 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"접근 권한이 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "삭제하려는 리뷰의 ID를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCourseReview(@PathVariable("id") Long id, @AuthenticationPrincipal LoginedInfo logined) {

        if(logined==null){
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[CourseReview] 리뷰 삭제 실패");
        }

        CourseReview courseReview = courseReviewService.findById(id);
        if (courseReview == null) {
            return ResponseEntity.notFound().build();
        }

        // 로그인한 사용자 닉네임 가져오기
        String nickname = logined.getNickname();
        if (!courseReview.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("작성자와 정보가 일치하지 않습니다.", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        courseReviewService.delete(id);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "코스 ID에 따른 리뷰 페이지네이션 조회", description = "특정 코스에 대한 리뷰를 페이지네이션하여 조회합니다.")
    @Parameter(name = "courseId", description = "리뷰를 조회할 코스의 ID", required = true)
    @Parameter(name = "record", description = "페이지당 표시할 데이터 수")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1부터 시작합니다.)")
    @GetMapping
    public ResponseEntity<CourseMakerPagination<ResponseCourseDto>> getAllCourseReviewsByCourseId(
            @RequestParam(name = "courseId") Long courseId,
            @RequestParam(defaultValue = "20", name = "record") int record,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @AuthenticationPrincipal LoginedInfo logined) {

        Pageable pageable = PageRequest.of(page - 1, record);

        CourseMakerPagination<CourseReview> reviewPage = courseReviewService.findAllByCourseId(courseId, pageable);
        List<CourseReview> reviewList = reviewPage.getContents();

        List<ResponseCourseDto> responseDtos = reviewList.stream()
                .map(review -> {
                    TravelCourse travelCourse = courseService.findById(review.getTravelCourse().getId());
                    Boolean isMyCourseReview = logined != null && logined.getNickname().equals(review.getMember().getNickname());
                    return ResponseCourseDto.toDto(travelCourse, review, isMyCourseReview);
                })
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseCourseDto> responseReviewPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(responseDtos, pageable, reviewPage.getTotalContents()),
                reviewPage.getTotalContents()
        );

        return ResponseEntity.ok(responseReviewPage);
    }

    @Operation(summary = "닉네임으로 코스 리뷰 조회", description = "특정 사용자의 닉네임을 통해 해당 사용자가 작성한 코스 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 닉네임으로 작성된 리뷰를 찾지 못할 때 반환", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 닉네임으로 작성된 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "nickname", description = "리뷰를 조회할 사용자의 닉네임", required = true)
    @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수", example = "20")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1부터 시작)", example = "1")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<CourseMakerPagination<ResponseCourseDto>> findCourseReviewByNickname(
            @PathVariable("nickname") String nickname,
            @RequestParam(defaultValue = "20", name = "record") Integer record,
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @AuthenticationPrincipal LoginedInfo logined) {

        Pageable pageable = PageRequest.of(page - 1, record);
        CourseMakerPagination<CourseReview> courseReviewPage = courseReviewService.findByMemberNickname(nickname, pageable);

        List<ResponseCourseDto> contents = courseReviewPage.getContents().stream()
                .map(courseReview -> {

                    Boolean isMyCourseReview = logined != null && logined.getNickname().equals(courseReview.getMember().getNickname());

                    TravelCourse travelCourse = courseService.findById(courseReview.getTravelCourse().getId());
                    return ResponseCourseDto.toDto(travelCourse, courseReview, isMyCourseReview);
                })
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseCourseDto> responseReviewPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(contents, pageable, courseReviewPage.getTotalContents()),
                courseReviewPage.getTotalContents()
        );

        return ResponseEntity.ok(responseReviewPage);
    }
}
