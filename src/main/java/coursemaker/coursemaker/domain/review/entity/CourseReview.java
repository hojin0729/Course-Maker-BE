package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter @Setter
@Table(name = "course_review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"memberId", "courseId"})
})
public class CourseReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @Column(name = "picture")
    private String picture;
}
