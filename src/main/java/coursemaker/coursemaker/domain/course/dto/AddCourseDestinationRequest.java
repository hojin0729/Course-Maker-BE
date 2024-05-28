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
    private short date;
    private Destination destination;

    public CourseDestination toEntity() {
        return CourseDestination.builder()
                .visitOrder(visitOrder)
                .date(date)
                .destination(destination)
                .build();
    }

    // 코스를 만들려는 시점에서는 travelcourse가 없다.
    // post 요청 시점에 course가 있다는건 말이 안 된다.
    // 그래서 travel course가 없어야한다.

}