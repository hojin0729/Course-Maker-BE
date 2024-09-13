package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "destination_review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"memberId", "destinationId"})
})
public class DestinationReview extends BaseEntity {

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
    @JoinColumn(name = "destinationId")
    private Destination destination;

    @ElementCollection
    @CollectionTable(name = "destination_review_pictures", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "picture")
    private List<String> pictures;

    @Column(name = "recommend_count")
    private Integer recommendCount;

}
