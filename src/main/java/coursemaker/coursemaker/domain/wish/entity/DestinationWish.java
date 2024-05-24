package coursemaker.coursemaker.domain.wish.entity;

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

    @Column(name = "count")
    private Long count;

    @ManyToOne
    @JoinColumn(name = "destinationId")
    private Destination destination;

//    @OneToOne
//    @JoinColumn(name = "memberId")
//    private Member member
}
