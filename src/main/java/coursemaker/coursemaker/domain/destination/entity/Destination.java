package coursemaker.coursemaker.domain.destination.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "name", length = 50)
    private String name;

    // Todo: 일단 50자, 실제 url길이 확인 후 수정
    @Column(name = "prictureLink", length = 50)
    private String prictureLink;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "latitude")
    private BigDecimal latitude;

    //Todo: BaseEntity에 있는 createdAt 교체 해야함
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "destination")
    private List<DestinationPicture> pictures;
}
