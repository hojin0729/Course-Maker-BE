package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
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

    @Column(name = "description",columnDefinition = "TEXT", length = 4000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private TravelCourse travelCourse;

    @ElementCollection
    @CollectionTable(name = "course_review_pictures", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "picture")
    private List<String> pictures;

    @Column(name = "recommend_count")
    private Integer recommendCount = 0;

    @CreationTimestamp
    @Column(name = "reviewed_at", updatable = false)
    private LocalDateTime reviewedAt;
}
