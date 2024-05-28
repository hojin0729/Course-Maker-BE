package coursemaker.coursemaker.domain.course.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "duration")
    private int duration;

    @Column(name = "travelerCount")
    private int travelerCount;

    @Column(name = "travelType")
    private int travelType;

    @Column(name = "pictureLink", length = 300)
    private String pictureLink;

//    @ManyToOne
//    @JoinColumn(name = "memberId", nullable = false)
//    private Member member;

    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseDestination> courseDestinations = new ArrayList<>();

    @Builder
    public TravelCourse(String title, String content, int duration, int travelerCount, int travelType, String pictureLink/*, Member member*/) {
        this.title = title;
        this.content = content;
        this.views = 0;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
        this.pictureLink = pictureLink;
//        this.member = member;
    }

    public void update(String title, String content, int duration, int travelerCount, int travelType, String pictureLink/*, Member member*/) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
        this.pictureLink = pictureLink;
//        this.member = member;
    }

    public void addCourseDestination(CourseDestination courseDestination) {
        courseDestinations.add(courseDestination);
        courseDestination.setTravelCourse(this);
    }

    public void incrementViews() {
        this.views++;
    }

}