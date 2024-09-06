package coursemaker.coursemaker.domain.like.entity;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "destination_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"destinationId", "memberId"}) // 유니크 제약 조건 추가
})
public class DestinationLike {

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