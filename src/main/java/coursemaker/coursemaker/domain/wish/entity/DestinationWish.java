package coursemaker.coursemaker.domain.wish.entity;

import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "destination_wish", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"destinationId", "memberId"})
})
public class DestinationWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "destinationId")
    private Destination destination;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "memberId")
    private Member member;
}