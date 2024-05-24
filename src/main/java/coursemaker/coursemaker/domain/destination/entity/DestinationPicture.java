package coursemaker.coursemaker.domain.destination.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DestinationPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destinationId")
    Destination destination;

    @Column(name = "pictureLink", length = 50)
    private String pictureLink;
}
