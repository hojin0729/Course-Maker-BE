package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestCourseDto {
    @Schema(hidden = true)
    private String nickname;

    @Schema(description = "리뷰 제목", example = "멋진 여행 코스!")
    @NotNull(message = "리뷰 제목을 입력하세요.")
    @NotBlank(message = "리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String title;

    @Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
    @NotNull(message = "리뷰 설명을 입력하세요.")
    @NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL", example = "http://example.com/review.jpg")
    @NotNull(message = "리뷰 사진 URL을 입력하세요.")
    @NotBlank(message = "리뷰 사진 URL은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String picture;

    @Schema(description = "평점", example = "4.5")
    @NotNull(message = "평점을 입력하세요.")
    private Double rating;

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
