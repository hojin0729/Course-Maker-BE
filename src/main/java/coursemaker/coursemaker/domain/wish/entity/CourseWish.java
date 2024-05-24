package coursemaker.coursemaker.domain.wish.entity;

import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CourseWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "count")
    private Long count;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

//    @OneToOne
//    @JoinColumn("memberId")
//    private Member member

}
