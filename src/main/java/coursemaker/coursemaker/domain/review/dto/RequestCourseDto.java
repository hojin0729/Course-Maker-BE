package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RequestCourseDto {
    @Schema(hidden = true)
    private String nickname;

    @Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
    @NotNull(message = "리뷰 설명을 입력하세요.")
    @NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Size(max = 4000, message = "리뷰 설명은 4000자를 초과할 수 없습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL 목록", example = "[\"http://example.com/review1.jpg\", \"http://example.com/review2.jpg\"]")
    @NotNull(message = "리뷰 사진 URL을 입력하세요.")
    private List<String> pictures;

    @Schema(description = "평점", example = "4.5")
    @NotNull(message = "평점을 입력하세요.")
    private Double rating;

    public CourseReview toEntity(Member member) {
        CourseReview courseReview = new CourseReview();
        courseReview.setMember(member);
        courseReview.setDescription(this.description);
        courseReview.setPictures(this.pictures);
        courseReview.setRating(this.rating);
        return courseReview;
    }
}
