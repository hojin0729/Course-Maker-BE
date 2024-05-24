package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import lombok.Getter;

@Getter
public class CourseDestinationResponse {

    private final short visitOrder;
    private final TravelCourse travelCourse;
//    private final Destination destination

    public CourseDestinationResponse(CourseDestination courseDestination) {
        this.visitOrder = courseDestination.getVisitOrder();
        this.travelCourse = courseDestination.getTravelCourse();
//        this.destination = courseDestination.getDestination();
    }
}
