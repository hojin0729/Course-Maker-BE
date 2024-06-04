package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;

import coursemaker.coursemaker.domain.tag.exception.IllegalTagArgumentException;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.exception.ErrorCode;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/courses")
public class CourseApiController {

    private final CourseService courseService;

    private final CourseDestinationService courseDestinationService;

    // POST
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 등록", description = "유저가 코스를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "코스 등록 성공, 헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @Parameter(
            name = "request",
            description = "등록할 코스의 세부 정보. 여기에는 코스의 이름(title), 내용(content), 조회수(views), 여행기간(duration), 여행인원(travelerCount), " +
                            "여행 타입(차, 대중교통, 도보 등)(travelType), 대표 이미지(pictureLink), 목적지 리스트(List<CourseDestination>) 등이 포함됩니다."
    )
/*********스웨거 어노테이션**********/
    @PostMapping
    public ResponseEntity<TravelCourse> createTravelCourse(@Valid @RequestBody AddTravelCourseRequest request) {
        TravelCourse savedTravelCourse = courseService.save(request);

        return (savedTravelCourse != null) ?
                ResponseEntity.created(URI.create("/v1/courses/" + savedTravelCourse.getId())).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    // GET
    /*********스웨거 어노테이션**********/
    @Operation(summary = "모든 여행 코스 조회", description = "조회수를 기준으로 정렬된 모든 여행 코스를 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)", schema = @Schema(type = "integer"))
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public ResponseEntity<Page<TravelCourseResponse>> findAllTravelCourse(@RequestParam(defaultValue = "20") @Min(1) int record,
                                                                          @RequestParam(defaultValue = "1") @Min(1) int page) {
        Pageable pageable = PageRequest.of(page - 1, record);
        Page<TravelCourse> travelCourses = courseService.getAllOrderByViewsDesc(pageable);
        Page<TravelCourseResponse> response = travelCourses.map(travelCourse -> {
            List<CourseDestinationResponse> courseDestinationResponses = travelCourse.getCourseDestinations().stream()
                    .map(courseDestinationService::toResponse)
                    .collect(Collectors.toList());
            return new TravelCourseResponse(travelCourse, courseDestinationResponses);
        });
        return ResponseEntity.ok(response);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "ID로 여행 코스 조회", description = "ID를 사용하여 특정 여행 코스를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "조회할 여행 코스의 ID")
    /*********스웨거 어노테이션**********/
    @GetMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> findTravelCourseById(@PathVariable long id) {
        TravelCourse travelCourse = courseService.findById(id);
        travelCourse = courseService.incrementViews(id);
        List<CourseDestinationResponse> courseDestinationResponses = travelCourse.getCourseDestinations().stream()
                .map(courseDestinationService::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new TravelCourseResponse(travelCourse, courseDestinationResponses));
    }

    // PUT
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 수정", description = "유저가 등록한 코스를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "id", description = "업데이트할 여행 코스의 ID")
    })
    /*********스웨거 어노테이션**********/
    @PutMapping("/{id}")
    public ResponseEntity<TravelCourse> updateTravelCourse(@PathVariable long id, @Valid @RequestBody UpdateTravelCourseRequest request) {
        TravelCourse updatedTravelCourse = courseService.update(id, request);

        return (updatedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    /*********스웨거 어노테이션**********/
    @Operation(summary = "여행 코스 삭제", description = "ID를 사용하여 특정 여행 코스를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "삭제할 코스의 ID")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelCourse(@PathVariable long id) {
        courseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @ExceptionHandler(TravelCourseDuplicatedException.class)
    public ResponseEntity<String> handleTagDuplicatedException(TagDuplicatedException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_COURSE.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(TravelCourseNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(TagNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_COURSE.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalTagArgumentException.class)
    public ResponseEntity<String> handleIllegalTravelCourseArgumentException(IllegalTravelCourseArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.ILLEGAL_COURSE_ARGUMENT.getStatus())
                .body(e.getMessage());
    }
}