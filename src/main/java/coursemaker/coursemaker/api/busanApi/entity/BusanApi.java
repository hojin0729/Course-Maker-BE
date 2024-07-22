package coursemaker.coursemaker.api.busanApi.entity;

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
@Table(name = "BusanApi")
public class BusanApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guganNm", columnDefinition = "TEXT")
    private String guganNm;

    @Column(name = "gmCourse", columnDefinition = "TEXT")
    private String gmCourse;

    @Column(name = "startAddr", columnDefinition = "TEXT")
    private String startAddr;

    @Column(name = "gmText", columnDefinition = "TEXT")
    private String gmText;

    @Column(name = "courseNm", columnDefinition = "TEXT")
    private String courseNm;

    @Column(name = "startPls", columnDefinition = "TEXT")
    private String startPls;

    @Column(name = "middlePls", columnDefinition = "TEXT")
    private String middlePls;

    @Column(name = "middleAdr", columnDefinition = "TEXT")
    private String middleAdr;

    @Column(name = "endPls", columnDefinition = "TEXT")
    private String endPls;

    @Column(name = "endAddr", columnDefinition = "TEXT")
    private String endAddr;

    @Column(name = "gmRange", columnDefinition = "TEXT")
    private String gmRange;

    @Column(name = "gmDegree", columnDefinition = "TEXT")
    private String gmDegree;

    @Column(name = "seq")
    private int seq;

//    @OneToOne
//    private Tag tag;
}