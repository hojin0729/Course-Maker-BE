package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_review_recommendation")
public class CourseReviewRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_review_id")
    private CourseReview courseReview;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
