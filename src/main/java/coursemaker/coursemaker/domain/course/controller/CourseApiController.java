package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseAlreadyDeletedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.exception.PictureNotFoundException;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import coursemaker.coursemaker.util.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/courses")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Course", description = "여행 코스 API")
public class CourseApiController {

    private final CourseService courseService;

    private final CourseDestinationService courseDestinationService;

    // POST
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 등록", description = "유저가 코스를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "코스 등록 성공, 헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content)
    })
/*********스웨거 어노테이션**********/
    @PostMapping
    public ResponseEntity<Void> createTravelCourse(@RequestBody @Valid AddTravelCourseRequest request, @LoginUser String nickname) {
        request.setNickname(nickname);
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
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content)
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)", schema = @Schema(type = "integer"))
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public ResponseEntity<CourseMakerPagination<TravelCourseResponse>> findAllTravelCourse(@RequestParam(defaultValue = "20", name = "record") Integer record,
                                                                                           @RequestParam(defaultValue = "1", name = "page") Integer page) {

        Pageable pageable = PageRequest.of(page-1, record);

        List<TravelCourseResponse> contents = new ArrayList<>();
        CourseMakerPagination<TravelCourse> travelCoursePage = courseService.getAllOrderByViewsDesc(pageable);
        int totalPage = travelCoursePage.getTotalPage();//
        List<TravelCourse> travelCourses = travelCoursePage.getContents();


        /*DTO - entity 변환*/
        for (TravelCourse travelCourse : travelCourses) {

            /*TODO: ROW MAPPER로 DTO-entity 변환*/
            List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(travelCourse)
                    .stream()
                    .map(courseDestinationService::toResponse)
                    .toList();

            contents.add(new TravelCourseResponse(travelCourse, courseDestinationResponses));
        }

        Page<TravelCourseResponse> responsePagepage = new PageImpl<>(contents, pageable, totalPage);

        CourseMakerPagination<TravelCourseResponse> response = new CourseMakerPagination<>(pageable, responsePagepage);

        return ResponseEntity.ok(response);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "ID로 여행 코스 조회", description = "ID를 사용하여 특정 여행 코스를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content)
    })
    /*********스웨거 어노테이션**********/
    @GetMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> findTravelCourseById(@PathVariable("id") Long id) {
        TravelCourse travelCourse = courseService.incrementViews(id);

        /*TODO: ROW MAPPER로 DTO-entity 변환*/
        List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(travelCourse)
                .stream()
                .map(courseDestinationService::toResponse)
                .toList();

        return ResponseEntity.ok(new TravelCourseResponse(travelCourse, courseDestinationResponses));
    }

    // PUT
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 수정", description = "유저가 등록한 코스를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content)
    })
    /*********스웨거 어노테이션**********/
    @PutMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> updateTravelCourse(@PathVariable("id") Long id, @Valid @RequestBody UpdateTravelCourseRequest request, @LoginUser String nickname) {
        request.setNickname(nickname);
        System.out.println("---------------------------------------------------id = " + id);
        TravelCourse updatedTravelCourse = courseService.update(id, request);

        /*TODO: ROW MAPPER로 DTO-entity 변환*/
        List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(updatedTravelCourse)
                .stream()
                .map(courseDestinationService::toResponse)
                .toList();

        TravelCourseResponse response = new TravelCourseResponse(updatedTravelCourse, courseDestinationResponses);

        return (updatedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    /*********스웨거 어노테이션**********/
    @Operation(summary = "여행 코스 삭제", description = "ID를 사용하여 특정 여행 코스를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content)
    })
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelCourse(@PathVariable("id") Long id) {
        courseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }
}