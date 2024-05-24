package coursemaker.coursemaker.domain.course.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelCourse{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @CreatedDate
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

//    @ManyToOne
//    @JoinColumn(name = "memberId", nullable = false)
//    private Member member;

    @Builder
    public TravelCourse(String title, String description, LocalDateTime createdAt/*, Member member*/) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
//        this.member = member;
    }

    public void update(String title, String description, LocalDateTime createdAt/*, Member member*/) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
//        this.member = member;
    }



}
