package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCourseDestinationRequest {

    private short visitOrder;
    private short date;
    private Destination destination;

    public CourseDestination toEntity() {
        return CourseDestination.builder()
                .visitOrder(visitOrder)
                .date(date)
                .destination(destination)
                .build();
    }
}