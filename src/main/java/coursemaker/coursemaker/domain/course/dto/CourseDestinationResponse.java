package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.Getter;

@Getter
public class CourseDestinationResponse {

    private final short visitOrder;
    private final short date;
    private final DestinationDto destination;

    public CourseDestinationResponse(CourseDestination courseDestination, DestinationDto destinationDto) {
        this.visitOrder = courseDestination.getVisitOrder();
        this.date = courseDestination.getDate();
        this.destination = destinationDto;
    }
}