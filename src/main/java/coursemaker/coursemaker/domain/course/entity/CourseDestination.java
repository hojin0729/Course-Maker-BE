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

    @Column(name = "date", nullable = false)
    private short date;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @ManyToOne
    @JoinColumn(name = "destinationId")
    private Destination destination;

    @Builder
    public CourseDestination(short visitOrder, short date, Destination destination) {
        this.visitOrder = visitOrder;
        this.date = date;
        this.destination = destination;
    }

    public void update(short visitOrder, short date, Destination destination) {
        this.visitOrder = visitOrder;
        this.date = date;
        this.destination = destination;
    }
}