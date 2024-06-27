package coursemaker.coursemaker.api.tourApi.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TourApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private long contentid;

    private String tel;
    private String addr1;
    private String addr2;
    private int sigungucode;
    private String firstimage;
    private String firstimage2;
    private double mapx;
    private double mapy;
    private String zipcode;
    private String createdtime;
    private String modifiedtime;
    private String cat1;
    private String cat2;
    private String cat3;
    private int contenttypeid;

    @Builder
    public TourApi(String title, long contentid, String tel, String addr1, String addr2, int sigungucode,
                   String firstimage, String firstimage2, double mapx, double mapy, String zipcode,
                   String createdtime, String modifiedtime, String cat1, String cat2, String cat3,
                   int contenttypeid) {
        this.title = title;
        this.contentid = contentid;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.sigungucode = sigungucode;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.zipcode = zipcode;
        this.createdtime = createdtime;
        this.modifiedtime = modifiedtime;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contenttypeid = contenttypeid;
    }
}

