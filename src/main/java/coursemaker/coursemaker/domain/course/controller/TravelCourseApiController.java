package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.service.TravelCourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/course/travelcourse")
public class TravelCourseApiController {

    @Autowired
    private final TravelCourseServiceImpl travelCourseService;

    @PostMapping
    public ResponseEntity<TravelCourse> addTravelCourse(@RequestBody AddTravelCourseRequest request) {
        TravelCourse savedTravelCourse = travelCourseService.save(request);

        return (savedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(savedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    public ResponseEntity<List<TravelCourseResponse>> findAllTravelCourse() {
        List<TravelCourseResponse> travelCourses = travelCourseService.findAll()
                .stream()
                .map(TravelCourseResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(travelCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelCourseResponse> findTravelCourse(@PathVariable long id) {
        TravelCourse travelCourse = travelCourseService.findById(id);

        return ResponseEntity.ok()
                .body(new TravelCourseResponse(travelCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelCourse(@PathVariable long id) {
        travelCourseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelCourse> updateTravelCourse(@PathVariable long id, @RequestBody UpdateTravelCourseRequest request) {
        TravelCourse updatedTravelCourse = travelCourseService.update(id, request);

        return (updatedTravelCourse != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedTravelCourse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
