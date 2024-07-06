package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ResponseCourseDto {
    private Long courseId;
    private String nickname;
    private String title;
    private String description;
    private String picture;
    private BigDecimal rating;

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
