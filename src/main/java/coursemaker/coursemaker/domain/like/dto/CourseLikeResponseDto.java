package coursemaker.coursemaker.domain.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseLikeResponseDto {


    @Schema(description = "코스의 Id", example = "1")
    private Long courseId;

    @Schema(description = "코스의 타이틀", example = "Course Title1")
    private String courseTitle;

    @Schema(hidden = true)
    private String memberNickname;

    public CourseLikeResponseDto(Long courseId, String courseTitle, String memberNickname) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.memberNickname = memberNickname;
    }
}