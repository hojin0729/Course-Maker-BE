package coursemaker.coursemaker.domain.tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DestinationTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    TODO: 목적지 도메인 엔티티 개발시 연결
    @OneToOne
    @JoinColumn(name = "Destination_id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag;
}
