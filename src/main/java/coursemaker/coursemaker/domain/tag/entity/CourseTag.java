package coursemaker.coursemaker.domain.tag.entity;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
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

    @ManyToOne
    @JoinColumn(name = "TravelCourse_id")
    private TravelCourse course;

    @ManyToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag;
}
