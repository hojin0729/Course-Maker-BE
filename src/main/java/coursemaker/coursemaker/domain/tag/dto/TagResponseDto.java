package coursemaker.coursemaker.domain.tag.dto;

import coursemaker.coursemaker.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TagResponseDto {

    @Schema(description = "태그의 Id", example = "1")
    private Long id;

    @Schema(description = "태그 이름", example = "연인")
    private String name;

    @Schema(description = "태그에 대한 설명", example = "그래서, 커플이시겠다?")
    private String description;

    public Tag toEntity(){
        Tag tag = new Tag();
        tag.setId(this.getId());
        tag.setName(this.getName());
        tag.setDescription(this.getDescription());
        return tag;
    }
}
