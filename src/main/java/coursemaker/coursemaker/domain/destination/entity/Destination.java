package coursemaker.coursemaker.domain.destination.entity;

import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Destination extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "pictureLink", length = 300)
    private String pictureLink;

    @Column(name = "views")
    private int views = 0;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "apiContent", columnDefinition = "TEXT")
    private String apiContent;

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "longitude", precision = 15, scale = 12)
    private BigDecimal longitude;

    @Column(name = "latitude", precision = 15, scale = 12)
    private BigDecimal latitude;

    @Column(name = "averageRating", nullable = false)
    private Double averageRating;

    @Column(name = "disabled")
    private Boolean disabled;

    // tourApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
    @Column(name = "contentId")
    private Long contentId;

    // busanApi에서 Destination DB로 저장될 때 중복된 데이터 판별 용으로 사용됩니다.
    @Column(name = "seq")
    private Integer seq;

    @Column(name = "isApiData")
    @ColumnDefault("false")
    private Boolean isApiData;

    @Column(name = "wishCount")
    private Integer wishCount;

    @Column(name = "reviewCount")
    private Integer reviewCount;

    @Column(name = "likeCount")
    private Integer likeCount;

    public void incrementViews() {
        this.views += 1;
    }

    // Soft delete 처리
    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
    }

    // Soft delete 상태 확인
//    public boolean isDeleted() {
//        return this.getDeletedAt() != null;
//    }
}