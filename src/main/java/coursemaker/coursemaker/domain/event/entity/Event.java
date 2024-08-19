package coursemaker.coursemaker.domain.event.entity;

import coursemaker.coursemaker.BaseEntity;
import coursemaker.coursemaker.domain.member.entity.Member;
import jakarta.persistence.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "picture")
    private String picture;

    @Column(name = "short_description")
    private String short_description;

    @Column(name = "description")
    private String description;
}
