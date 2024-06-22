package coursemaker.coursemaker.domain.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TagUpdateDto {
    @Schema(description = "수정하려는 태그의 Id", example = "1")
    @NotNull(message = "태그 ID를 입력해주세요.")
    private Long id;

    @Schema(description = "수정된 태그 이름", example = "연인")
    @NotNull(message = "태그 이름을 입력하세요.")
    @NotBlank(message = "태그 이름은 공백 혹은 빈 문자는 혀용하지 않습니다.")
    private String name;

    @Schema(description = "수정된 태그에 대한 설명", example = "그래서, 커플이시겠다?")
    @NotNull(message = "태그에 대한 설명을 입력하세요.")
    @NotBlank(message = "태그에 대한 설명은 공백 혹은 빈 문자는 혀용하지 않습니다.")
    private String description;
}
