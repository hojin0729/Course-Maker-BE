package coursemaker.coursemaker.domain.event.dto;

import coursemaker.coursemaker.domain.event.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseEventDto {
    @Schema(hidden = true)
    private Long id;
    private String title;
    private String picture;
    private String shortDescription;
    private String description;

    public static ResponseEventDto toDto(Event event) {
        ResponseEventDto dto = new ResponseEventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setPicture(event.getPicture());
        dto.setShortDescription(event.getShort_description());
        dto.setDescription(event.getDescription());
        return dto;
    }
}
