package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.course.service.CourseServiceImpl;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/courses")
public class CourseApiController {

    @Autowired
    private final CourseService courseService;

    // 코스를 만들려고 한다.
    // 코스 생성 버튼을 누른다.
    // 그러면 여행지를 먼저 불러와야한다. (get)이다.
    // Destination 컨트롤러에서 어떻게든 불러와서 데스티네이션 정보가 들어온다.
    // 순서랑 이런 것들이 들어온다.
    // courseDestination 테이블에서 destinationId를 통해 데스티네이션이 불러와지고,
    // 거기에 추가로 date와 visitOrder이 부여된다. (dto)로 처리한다.
    // 코스 쪽으로 포스트 요청을 하면 코스가 생성된다.


    // POST
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 등록", description = "유저가 코스를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "코스 등록 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @Parameter(name = "request", description = "코스 등록 시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PostMapping
    public ResponseEntity<TravelCourse> createTravelCourse(@RequestBody AddTravelCourseRequest request) {
        TravelCourse savedTravelCourse = courseService.save(request);

        return (savedTravelCourse != null) ?
                // ResponseEntity.status(HttpStatus.CREATED).body(savedTravelCourse) :
                ResponseEntity.created(URI.create("/v1/course/" + savedTravelCourse.getId())).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    // GET
    /*********스웨거 어노테이션**********/
    @Operation(summary = "등록된 코스 목록 조회", description = "유저들이 등록한 코스들을 보여줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "코스 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourseResponse.class)))
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public ResponseEntity<Page<TravelCourseResponse>> findAllTravelCourse(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<TravelCourse> travelCourses = courseService.findAllOrderByViewsDesc(pageable);
        Page<TravelCourseResponse> response = travelCourses.map(TravelCourseResponse::new);
        return ResponseEntity.ok(response);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 상세 조회", description = "유저가 등록한 코스의 상세페이지를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "코스 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "코스의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @GetMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> findTravelCourseById(@PathVariable long id) {
        TravelCourse travelCourse = courseService.findById(id);
        travelCourse = courseService.incrementViews(id);
        return ResponseEntity.ok(new TravelCourseResponse(travelCourse));
    }

    // PUT
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 수정", description = "유저가 등록한 코스를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 수정 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "코스의 고유 id 번호")
    @Parameter(name = "request", description = "코스 수정 시 입력되는 요소들")
    /*********스웨거 어노테이션**********/
    @PutMapping("/{id}")
    public ResponseEntity<TravelCourse> updateTravelCourse(@PathVariable long id, @RequestBody UpdateTravelCourseRequest request) {
        TravelCourse updatedTravelCourse = courseService.update(id, request);

        return (updatedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 삭제", description = "유저가 등록한 코스를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "코스의 고유 id 번호")
    /*********스웨거 어노테이션**********/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelCourse(@PathVariable long id) {
        courseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }



    // CourseDestination

    @Operation(summary = "코스에 여행지 추가", description = "유저가 코스에 여행지를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "코스 목적지 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping("/{id}/destinations")
    public ResponseEntity<CourseDestination> addCourseDestination(@PathVariable long id, @RequestBody AddCourseDestinationRequest request) {
        TravelCourse travelCourse = courseService.findById(id);
        CourseDestination courseDestination = request.toEntity();
        courseDestination.setTravelCourse(travelCourse);
        CourseDestination savedCourseDestination = courseService.addCourseDestination(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseDestination);
    }

    @Operation(summary = "코스 목적지 수정", description = "유저가 코스의 목적지를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 목적지 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
    })
    @PutMapping("/destinations/{destinationId}")
    public ResponseEntity<CourseDestination> updateCourseDestination(@PathVariable long destinationId, @RequestBody UpdateCourseDestinationRequest request) {
        CourseDestination updatedCourseDestination = courseService.updateCourseDestination(destinationId, request);
        return ResponseEntity.ok(updatedCourseDestination);
    }

    @Operation(summary = "코스 목적지 삭제", description = "유저가 코스의 목적지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 목적지 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
    })
    @DeleteMapping("/destinations/{destinationId}")
    public ResponseEntity<Void> deleteCourseDestination(@PathVariable long destinationId) {
        courseService.deleteCourseDestination(destinationId);
        return ResponseEntity.noContent().build();
    }

}