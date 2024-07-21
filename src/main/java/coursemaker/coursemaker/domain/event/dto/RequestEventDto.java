package coursemaker.coursemaker.domain.event.dto;

import coursemaker.coursemaker.domain.event.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestEventDto {
    @Schema(hidden = true)
    private Long id;
    private String title;
    private String picture;
    private String shortDescription;
    private String description;

    public Event toEntity() {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setPicture(picture);
        event.setShort_description(shortDescription);
        event.setDescription(description);
        return event;
    }
}
