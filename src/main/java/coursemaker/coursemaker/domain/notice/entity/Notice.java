package coursemaker.coursemaker.domain.notice.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description")
    private String description;
}
