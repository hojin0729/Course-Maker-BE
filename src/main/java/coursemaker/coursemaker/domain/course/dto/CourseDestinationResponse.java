package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CourseDestinationResponse {

    private final Short visitOrder;
    private final Short date;
    private final DestinationDto destination;

    public CourseDestinationResponse(CourseDestination courseDestination, DestinationDto destinationDto) {
        this.visitOrder = courseDestination.getVisitOrder();
        this.date = courseDestination.getDate();
        this.destination = destinationDto;
    }
}