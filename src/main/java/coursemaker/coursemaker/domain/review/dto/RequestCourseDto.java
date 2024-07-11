package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestCourseDto {
    @Schema(hidden = true)
    private String nickname;
    private String title;
    private String description;
    private String picture;
    private BigDecimal rating;

    public CourseReview toEntity(Member member) {
        CourseReview courseReview = new CourseReview();
        courseReview.setMember(member);
        courseReview.setTitle(this.title);
        courseReview.setDescription(this.description);
        courseReview.setPicture(this.picture);
        courseReview.setRating(this.rating);
        return courseReview;
    }
}
