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
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/courses")
public class CourseApiController {

    @Autowired
    private final CourseService courseService;

    // POST
    /*********스웨거 어노테이션**********/
    @Operation(summary = "코스 등록", description = "유저가 코스를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "코스 등록 성공, 헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다.",
                    content = @Content(schema = @Schema(implementation = TravelCourse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @Parameter(
            name = "request",
            description = "등록할 코스의 세부 정보. 여기에는 코스의 이름(title), 내용(content), 조회수(views), 여행기간(duration), 여행인원(travelerCount), " +
                            "여행 타입(차, 대중교통, 도보 등)(travelType), 대표 이미지(pictureLink), 목적지 리스트(List<CourseDestination>) 등이 포함됩니다.",
            schema = @Schema(implementation = AddTravelCourseRequest.class)
    )
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
    @Operation(summary = "모든 여행 코스 조회", description = "조회수를 기준으로 정렬된 모든 여행 코스를 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TravelCourseResponse.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "page", description = "조회할 페이지 번호(페이지는 1 페이지 부터 시작합니다.)", schema = @Schema(type = "integer"))
    })
    /*********스웨거 어노테이션**********/
    @GetMapping
    public ResponseEntity<Page<TravelCourseResponse>> findAllTravelCourse(@RequestParam(defaultValue = "0", name = "record") int record,
                                                                          @RequestParam(name = "page") int page) {
        Pageable pageable = PageRequest.of(page-1, record);
        Page<TravelCourse> travelCourses = courseService.getAllOrderByViewsDesc(pageable);
        Page<TravelCourseResponse> response = travelCourses.map(TravelCourseResponse::new);
        return ResponseEntity.ok(response);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "ID로 여행 코스 조회", description = "ID를 사용하여 특정 여행 코스를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameter(name = "id", description = "조회할 여행 코스의 ID")
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
            @ApiResponse(responseCode = "200", description = "업데이트 성공",
                    content = @Content(schema = @Schema(implementation = TravelCourse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "id", description = "업데이트할 여행 코스의 ID"),
            @Parameter(name = "request", description = "업데이트할 코스의 세부 정보, 여기에는 코스의 이름(title), 내용(content), 조회수(views), 여행기간(duration), 여행인원(travelerCount), " +
                    "여행 타입(차, 대중교통, 도보 등)(travelType), 대표 이미지(pictureLink), 목적지 리스트(List<CourseDestination>) 등이 포함됩니다.",
                    schema = @Schema(implementation = UpdateTravelCourseRequest.class))
    })
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




    // CourseDestination -------





//    @Operation(summary = "코스에 여행지 추가", description = "유저가 특정 코스에 여행지를 추가합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "추가 성공, 헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다.",
//                    content = @Content(schema = @Schema(implementation = CourseDestination.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
//            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
//    })
//    @Parameters({
//            @Parameter(name = "id", description = "여행지를 추가할 코스의 ID"),
//            @Parameter(name = "request", description = "코스에 등록할 여행지의 세부 정보, 여기에는 여행지 정보(destination), " +
//                    "여행 순서(visitOrder), 며칠 째 여행인지(ex: 1일차 여행, 2일차 여행)(date) 등이 포함됩니다.",
//                    schema = @Schema(implementation = AddCourseDestinationRequest.class))
//    })
//    @PostMapping("/{id}/destinations")
//    public ResponseEntity<CourseDestination> addCourseDestination(@PathVariable long id, @RequestBody AddCourseDestinationRequest request) {
//        TravelCourse travelCourse = courseService.findById(id);
//        CourseDestination courseDestination = request.toEntity();
//        courseDestination.setTravelCourse(travelCourse);
//        CourseDestination savedCourseDestination = courseService.addCourseDestination(request);
//        // return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseDestination);
//        return ResponseEntity.created(URI.create("/v1/course/" + travelCourse.getId() + "destinations")).build();
//    }

//    @Operation(summary = "모든 코스 여행지 조회", description = "모든 코스의 여행지를 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공",
//                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourseDestinationResponse.class))))
//    })
//    @GetMapping("/destinations")
//    public ResponseEntity<List<CourseDestinationResponse>> findAllCourseDestinations() {
//        List<CourseDestinationResponse> courseDestinations = courseService.findAllCourseDestinations();
//        return ResponseEntity.ok(courseDestinations);
//    }
//
//    @Operation(summary = "destinationId로 코스 여행지 조회", description = "destinationId를 사용하여 특정 코스 여행지를 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공",
//                    content = @Content(schema = @Schema(implementation = CourseDestinationResponse.class))),
//            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
//    })
//    @Parameter(name = "destinationId", description = "조회할 코스 여행지의 ID")
//    @GetMapping("/destinations/{destinationId}")
//    public ResponseEntity<CourseDestinationResponse> findCourseDestinationById(@PathVariable long destinationId) {
//        CourseDestinationResponse courseDestination = courseService.findCourseDestinationById(destinationId);
//        return ResponseEntity.ok(courseDestination);
//    }

//    @Operation(summary = "코스 여행지 수정", description = "ID를 사용하여 특정 코스 여행지를 수정합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "수정 성공",
//                    content = @Content(schema = @Schema(implementation = CourseDestination.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
//            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
//    })
//    @Parameters({
//            @Parameter(name = "destinationId", description = "수정할 코스 여행지의 ID"),
//            @Parameter(name = "request", description = "업데이트할 여행지의 세부 정보, 여기에는 여행지 정보(destination), " +
//                    "여행 순서(visitOrder), 며칠 째 여행인지(ex: 1일차 여행, 2일차 여행)(date) 등이 포함됩니다.",
//                    schema = @Schema(implementation = UpdateCourseDestinationRequest.class))
//    })
//    @PutMapping("/destinations/{destinationId}")
//    public ResponseEntity<CourseDestination> updateCourseDestination(@PathVariable long destinationId, @RequestBody UpdateCourseDestinationRequest request) {
//        CourseDestination updatedCourseDestination = courseService.updateCourseDestination(destinationId, request);
//        return ResponseEntity.ok(updatedCourseDestination);
//    }

//    @Operation(summary = "코스 여행지 삭제", description = "유저가 코스의 여행지를 삭제합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "코스 여행지 삭제 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
//            @ApiResponse(responseCode = "404", description = "코스를 찾을 수 없음")
//    })
//    @Parameter(name = "destinationId", description = "삭제할 코스의 ID")
//    @DeleteMapping("/destinations/{destinationId}")
//    public ResponseEntity<Void> deleteCourseDestination(@PathVariable long destinationId) {
//        courseService.deleteCourseDestination(destinationId);
//        return ResponseEntity.noContent().build();
//    }

}