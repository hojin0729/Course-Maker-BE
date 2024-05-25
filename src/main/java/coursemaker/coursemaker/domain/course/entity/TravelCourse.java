package coursemaker.coursemaker.domain.course.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String content;

    @Column(name = "duration")
    private int duration;

    @Column(name = "travelerCount")
    private int travelerCount;

    @Column(name = "travelType")
    private int travelType;

//    @ManyToOne
//    @JoinColumn(name = "memberId", nullable = false)
//    private Member member;

    @Builder
    public TravelCourse(String title, String content, int duration, int travelerCount, int travelType/*, Member member*/) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
//        this.member = member;
    }

    public void update(String title, String content, int duration, int travelerCount, int travelType/*, Member member*/) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
//        this.member = member;
    }



}
