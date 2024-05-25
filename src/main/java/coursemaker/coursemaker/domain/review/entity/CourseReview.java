package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter @Setter
public class CourseReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

//    @ManyToOne
//    @JoinColumn(name = "memberId")
//    private Member member
}
