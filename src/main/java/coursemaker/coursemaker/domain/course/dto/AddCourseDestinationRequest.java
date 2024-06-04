package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.*;

@Data
public class AddCourseDestinationRequest {

    private Short visitOrder;
    private Short date;
    private Destination destination;

    public CourseDestination toEntity() {
        return CourseDestination.builder()
                .visitOrder(visitOrder)
                .date(date)
                .destination(destination)
                .build();
    }
}