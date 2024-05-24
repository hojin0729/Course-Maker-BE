package coursemaker.coursemaker.domain.course.controller;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.service.CourseDestinationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/course/coursedestination")
public class CourseDestinationApiController {

    @Autowired
    private final CourseDestinationServiceImpl courseDestinationService;

    @PostMapping
    public ResponseEntity<CourseDestination> addCourseDestination(@RequestBody AddCourseDestinationRequest request) {
        CourseDestination savedCourseDestination = courseDestinationService.save(request);

        return (savedCourseDestination != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(savedCourseDestination) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    public ResponseEntity<List<CourseDestinationResponse>> findAllCourseDestinations() {
        List<CourseDestinationResponse> courseDestinations = courseDestinationService.findAll()
                .stream()
                .map(CourseDestinationResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(courseDestinations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDestinationResponse> findCourseDestination(@PathVariable long id) {
        CourseDestination courseDestination = courseDestinationService.findById(id);

        return ResponseEntity.ok()
                .body(new CourseDestinationResponse(courseDestination));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDestination> deleteCourseDestination(@PathVariable long id) {
        courseDestinationService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDestination> updateCourseDestination(@PathVariable long id, @RequestBody UpdateCourseDestinationRequest request) {
        CourseDestination updatedCourseDestination = courseDestinationService.update(id, request);

        return (updatedCourseDestination != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedCourseDestination) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}

