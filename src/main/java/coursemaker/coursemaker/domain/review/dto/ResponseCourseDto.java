package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Data
public class ResponseCourseDto {
    @Schema(description = "코스 ID", example = "1")
    @NotNull(message = "코스 ID를 입력하세요.")
    private Long courseId;

    @Schema(description = "리뷰의 고유 id", example = "1")
    private Long reviewId;

    @Schema(description = "리뷰 작성자 닉네임", example = "coursemaker")
    @NotNull(message = "리뷰 작성자 닉네임을 입력하세요.")
    @NotBlank(message = "리뷰 작성자 닉네임은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String nickname;

    @Schema(description = "리뷰 설명", example = "이 코스는 정말 멋졌어요! 경치가 아름답고, 음식도 맛있었습니다.")
    @NotNull(message = "리뷰 설명을 입력하세요.")
    @NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL 목록", example = "[\"http://example.com/review1.jpg\", \"http://example.com/review2.jpg\"]")
    @NotNull(message = "리뷰 사진 URL을 입력하세요.")
    private List<String> pictures;

    @Schema(description = "평점", example = "4.5")
    @NotNull(message = "평점을 입력하세요.")
    private Double rating;

    @Schema(description = "내가 작성한 리뷰인지 여부", example = "true")
    private Boolean isMyCourseReview;

    @Schema(description = "내가 좋아요를 누른 리뷰인지 여부", example = "true")
    private Boolean isMyLikeReview;

    @Schema(description = "리뷰 좋아요 수", example = "10")
    private Integer recommendCount;

    @Schema(description = "리뷰 작성 날짜", example = "2024-09-14")
    private String createdAt;

    public static ResponseCourseDto toDto(TravelCourse travelCourse, CourseReview courseReview, Boolean isMyCourseReview, Boolean isMyLikeReview) {
        ResponseCourseDto dto = new ResponseCourseDto();
        dto.setCourseId(travelCourse.getId());
        dto.setNickname(courseReview.getMember().getNickname());
        dto.setDescription(courseReview.getDescription());
        dto.setPictures(courseReview.getPictures());
        dto.setRating(courseReview.getRating());
        dto.setReviewId(courseReview.getId());
        dto.setIsMyCourseReview(isMyCourseReview);
        dto.setIsMyLikeReview(isMyLikeReview);
        dto.setRecommendCount(courseReview.getRecommendCount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dto.setCreatedAt(courseReview.getCreatedAt().format(formatter));
        return dto;
    }
}
