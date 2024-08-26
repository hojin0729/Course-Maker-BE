package coursemaker.coursemaker.domain.course.entity;

import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TravelCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "duration")
    private int duration;

    @Column(name = "travelerCount")
    private int travelerCount;

    @Column(name = "travelType")
    @ColumnDefault("0")
    private Integer travelType;

    @Column(name = "pictureLink", length = 300)
    private String pictureLink;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

//    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CourseDestination> courseDestinations = new ArrayList<>();

    // TODO: 코스태그 - 코스간에 연관관계를 잘 공부해보세여.
    // TODO: 여기서 순환참조가 터졌습니다.
    // TODO: 오류 메시지: Could not write JSON: Infinite recursion (StackOverflowError)
//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CourseTag> courseTags = new ArrayList<>();

    @Builder
    public TravelCourse(String title, String content, int duration, int travelerCount, int travelType, String pictureLink, Member member) {
        this.title = title;
        this.content = content;
        this.views = 0;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
        this.pictureLink = pictureLink;
        this.member = member;
    }

    public void update(String title, String content, int duration, int travelerCount, int travelType, String pictureLink) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.travelerCount = travelerCount;
        this.travelType = travelType;
        this.pictureLink = pictureLink;
    }

//    public void addCourseDestination(CourseDestination courseDestination) {
//        courseDestinations.add(courseDestination);
//        courseDestination.setTravelCourse(this);
//    }
//
//    public void updateCourseDestination(CourseDestination courseDestination) {
//        courseDestinations.add(courseDestination);
//        courseDestination.setTravelCourse(this);
//    }

    public void incrementViews() {
        this.views++;
    }
}