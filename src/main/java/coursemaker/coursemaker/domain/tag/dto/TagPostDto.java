package coursemaker.coursemaker.domain.tag.dto;

import coursemaker.coursemaker.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Data;

@Data
public class TagPostDto {

    private String name;
    private String description;

    public Tag toEntity() {
        Tag tag = new Tag();
        tag.setName(this.name);
        tag.setDescription(this.description);
        return tag;
    }
}
