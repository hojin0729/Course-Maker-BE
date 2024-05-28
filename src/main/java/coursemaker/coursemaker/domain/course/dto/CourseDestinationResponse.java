package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.Getter;

@Getter
public class CourseDestinationResponse {

    private final short visitOrder;
    private final short date;
    private final Destination destination;

    public CourseDestinationResponse(CourseDestination courseDestination) {
        this.visitOrder = courseDestination.getVisitOrder();
        this.date = courseDestination.getDate();
        this.destination = courseDestination.getDestination();
    }
}