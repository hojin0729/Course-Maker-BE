package coursemaker.coursemaker.domain.like.entity;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CourseLike {

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
