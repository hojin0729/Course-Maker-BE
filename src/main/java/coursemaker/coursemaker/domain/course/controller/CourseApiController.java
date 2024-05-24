package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.service.CourseServiceImpl;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/course")
public class CourseApiController {

    @Autowired
    private final CourseServiceImpl courseService;

//    @PostMapping("/destination")
//    public ResponseEntity<CourseDestination> addCourseDestination(@RequestBody AddCourseDestinationRequest request) {
//        CourseDestination savedCourseDestination = courseService.courseDestinationSave(request);
//
//        return (savedCourseDestination != null) ?
//                ResponseEntity.status(HttpStatus.CREATED).body(savedCourseDestination) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }
//
//    @GetMapping("/destination")
//    public ResponseEntity<List<CourseDestinationResponse>> findAllCourseDestinations() {
//        List<CourseDestinationResponse> courseDestinations = courseService.courseDestinationFindAll()
//                .stream()
//                .map(CourseDestinationResponse::new)
//                .toList();
//
//        return ResponseEntity.ok()
//                .body(courseDestinations);
//    }
//
//    @GetMapping("/destination/{id}")
//    public ResponseEntity<CourseDestinationResponse> findCourseDestination(@PathVariable long id) {
//        CourseDestination courseDestination = courseService.courseDestinationFindById(id);
//
//        return ResponseEntity.ok()
//                .body(new CourseDestinationResponse(courseDestination));
//    }
//
//    @DeleteMapping("/destination/{id}")
//    public ResponseEntity<CourseDestination> deleteCourseDestination(@PathVariable long id) {
//        courseService.courseDestinationDelete(id);
//
//        return ResponseEntity.ok()
//                .build();
//    }
//
//    @PutMapping("/destination/{id}")
//    public ResponseEntity<CourseDestination> updateCourseDestination(@PathVariable long id, @RequestBody UpdateCourseDestinationRequest request) {
//        CourseDestination updatedCourseDestination = courseService.courseDestinationUpdate(id, request);
//
//        return (updatedCourseDestination != null) ?
//                ResponseEntity.status(HttpStatus.OK).body(updatedCourseDestination) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }




    @PostMapping
    public ResponseEntity<TravelCourse> addTravelCourse(@RequestBody AddTravelCourseRequest request) {
        TravelCourse savedTravelCourse = courseService.save(request);

        return (savedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(savedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    public ResponseEntity<List<TravelCourseResponse>> findAllTravelCourse() {
        List<TravelCourseResponse> travelCourses = courseService.findAll()
                .stream()
                .map(TravelCourseResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(travelCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> findTravelCourse(@PathVariable long id) {
        TravelCourse travelCourse = courseService.findById(id);

        return ResponseEntity.ok()
                .body(new TravelCourseResponse(travelCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelCourse(@PathVariable long id) {
        courseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelCourse> updateTravelCourse(@PathVariable long id, @RequestBody UpdateTravelCourseRequest request) {
        TravelCourse updatedTravelCourse = courseService.update(id, request);

        return (updatedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
