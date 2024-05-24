package coursemaker.coursemaker.domain.review.entity;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class DestinationReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "destinationId")
    private Destination destination;

//    @ManyToOne
//    @JoinColumn(name = "memberId")
//    private Member member
}
