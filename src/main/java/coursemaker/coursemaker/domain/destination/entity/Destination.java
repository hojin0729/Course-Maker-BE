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

    // Todo: 대표사진 URL 길이가 50이 넘으면 수정
    @Column(name = "pictureLink", length = 50)
    private String pictureLink;

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
}
