package coursemaker.coursemaker.domain.tag.dto;

import coursemaker.coursemaker.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TagDto {
    private String name;
    private String description;

    public static Tag toEntity(TagDto dto){
        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setDescription(dto.getDescription());
        return tag;
    }
}
