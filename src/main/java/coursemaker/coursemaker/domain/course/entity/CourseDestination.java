package coursemaker.coursemaker.domain.course.entity;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "visitOrder", nullable = false)
    private short visitOrder;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @OneToOne
    @JoinColumn(name = "destinationId")
    private Destination destination;

    @Builder
    public CourseDestination(short visitOrder, TravelCourse travelCourse, Destination destination) {
        this.visitOrder = visitOrder;
        this.travelCourse = travelCourse;
        this.destination = destination;
    }

    public void update(short visitOrder, TravelCourse travelCourse, Destination destination) {
        this.visitOrder = visitOrder;
        this.travelCourse = travelCourse;
        this.destination = destination;
    }
}
