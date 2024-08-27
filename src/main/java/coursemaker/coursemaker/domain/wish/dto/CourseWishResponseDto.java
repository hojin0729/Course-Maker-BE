package coursemaker.coursemaker.domain.wish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseWishResponseDto {

    @Schema(description = "코스찜 Id", example = "1")
    private Long id;

    @Schema(description = "코스의 Id", example = "1")
    private Long courseId;

    @Schema(description = "코스의 타이틀", example = "Course Title1")
    private String courseTitle;

    @Schema(hidden = true)
    private String memberNickname;

    public CourseWishResponseDto(Long id, Long courseId, String courseTitle, String memberNickname) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.memberNickname = memberNickname;
    }
}