package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCourseDestinationRequest {

    private short visitOrder;
    private TravelCourse travelCourse;
    private Destination destination;

    public CourseDestination toEntity() {
        return CourseDestination.builder()
                .visitOrder(visitOrder)
                .travelCourse(travelCourse)
                .destination(destination)
                .build();
    }

}

