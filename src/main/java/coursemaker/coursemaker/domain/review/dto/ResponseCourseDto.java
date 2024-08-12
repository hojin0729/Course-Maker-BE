package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseCourseDto {
    @Schema(description = "코스 ID", example = "1")
    private Long courseId;

    @Schema(description = "리뷰 작성자 닉네임", example = "coursemaker")
    private String nickname;

    @Schema(description = "리뷰 제목", example = "멋진 여행 코스!")
    private String title;

    @Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL", example = "http://example.com/review.jpg")
    private String picture;

    @Schema(description = "평점", example = "4.5")
    private Double rating;

    public static ResponseCourseDto toDto(TravelCourse travelCourse, CourseReview courseReview) {
        ResponseCourseDto dto = new ResponseCourseDto();
        dto.setCourseId(travelCourse.getId());
        dto.setNickname(courseReview.getMember().getNickname());
        dto.setTitle(courseReview.getTitle());
        dto.setDescription(courseReview.getDescription());
        dto.setPicture(courseReview.getPicture());
        dto.setRating(courseReview.getRating());
        return dto;
    }
}
