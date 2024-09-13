package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "destination_review_recommendation")
public class DestinationReviewRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private DestinationReview destinationReview;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
