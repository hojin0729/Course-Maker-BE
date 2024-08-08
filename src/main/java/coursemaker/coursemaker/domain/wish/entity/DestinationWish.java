package coursemaker.coursemaker.domain.wish.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DestinationWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destinationId")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}