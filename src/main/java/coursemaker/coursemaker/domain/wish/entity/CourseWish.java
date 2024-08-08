package coursemaker.coursemaker.domain.wish.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
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

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

}
