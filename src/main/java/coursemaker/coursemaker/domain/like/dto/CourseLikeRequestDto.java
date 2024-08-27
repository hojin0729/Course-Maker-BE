package coursemaker.coursemaker.domain.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseLikeRequestDto {

    @Schema(description = "코스의 Id", example = "1")
    @NotNull(message = "코스 ID를 입력해주세요.")
    private Long courseId;

    @Schema(hidden = true)
    @NotNull(message = "사용자 nickname을 입력해주세요.")
    private String nickname;

    public CourseLikeRequestDto() {}

    public CourseLikeRequestDto(Long courseId, String nickname) {
        this.courseId = courseId;
        this.nickname = nickname;
    }
}