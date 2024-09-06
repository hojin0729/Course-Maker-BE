package coursemaker.coursemaker.domain.wish.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "course_wish", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"courseId", "memberId"})
})
public class CourseWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "memberId")
    private Member member;

}
