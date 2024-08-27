package coursemaker.coursemaker.domain.wish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseWishRequestDto {

    @Schema(description = "코스의 Id", example = "1")
    @NotNull(message = "코스 ID를 입력해주세요.")
    private Long courseId;

    @Schema(description = "사용자의 nickname", example = "nickname1")
    @NotNull(message = "사용자 nickname을 입력해주세요.")
    private String nickname;

    public CourseWishRequestDto() {}

    public CourseWishRequestDto(Long courseId, String nickname) {
        this.courseId = courseId;
        this.nickname = nickname;
    }
}