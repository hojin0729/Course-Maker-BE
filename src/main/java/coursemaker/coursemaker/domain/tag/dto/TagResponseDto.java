package coursemaker.coursemaker.domain.tag.dto;

import coursemaker.coursemaker.domain.tag.entity.Tag;
import lombok.Data;

@Data
public class TagResponseDto {
    private Long id;
    private String name;
    private String description;

    public Tag toEntity(){
        Tag tag = new Tag();
        tag.setId(this.getId());
        tag.setName(this.getName());
        tag.setDescription(this.getDescription());
        return tag;
    }
}
