package coursemaker.coursemaker.api.tourApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourApi")
public class TourApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "contentid")
    private long contentid;

    @Column(name = "tel", columnDefinition = "TEXT")
    private String tel;

    @Column(name = "addr1", columnDefinition = "TEXT")
    private String addr1;

    @Column(name = "addr2", columnDefinition = "TEXT")
    private String addr2;

    @Column(name = "sigungucode")
    private int sigungucode;

    @Column(name = "firstimage", columnDefinition = "TEXT")
    private String firstimage;

    @Column(name = "firstimage2", columnDefinition = "TEXT")
    private String firstimage2;

    @Column(name = "mapx", precision = 15, scale = 12)
    private BigDecimal mapx;

    @Column(name = "mapy", precision = 15, scale = 12)
    private BigDecimal mapy;

    @Column(name = "zipcode", length = 20)
    private String zipcode;

    @Column(name = "createdtime", length = 50)
    private String createdtime;

    @Column(name = "modifiedtime", length = 50)
    private String modifiedtime;

    @Column(name = "cat1", length = 10)
    private String cat1;

    @Column(name = "cat2", length = 10)
    private String cat2;

    @Column(name = "cat3", length = 10)
    private String cat3;

    @Column(name = "contenttypeid")
    private int contenttypeid;

    @Column(name = "disabled")
    private Long disabled;

    @Column(name = "homepage", columnDefinition = "TEXT")
    private String homepage;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

//    @OneToOne
//    private Tag tag;
}