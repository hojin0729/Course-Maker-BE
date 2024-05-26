package coursemaker.coursemaker.domain.tag.entity;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "DestinationTag")
public class DestinationTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "Destination_id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag;
}
