package coursemaker.coursemaker.domain.tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "CourseTag")
public class CourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    TODO: 코스 도메인 엔티티 개발시 연결
    @OneToOne
    @JoinColumn(name = "TravelCourse_id")
    private TravelCourse course;

    @ManyToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag;
}
