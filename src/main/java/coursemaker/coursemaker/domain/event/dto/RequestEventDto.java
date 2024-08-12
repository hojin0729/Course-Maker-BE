package coursemaker.coursemaker.domain.event.dto;

import coursemaker.coursemaker.domain.event.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestEventDto {
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "이벤트 제목", example = "여름 페스티벌 2024")
    @NotNull(message = "이벤트 제목을 입력하세요.")
    @NotBlank(message = "이벤트 제목은 공백이나 빈 문자를 허용하지 않습니다.")
    private String title;

    @Schema(description = "이벤트 이미지 URL", example = "http://example.com/event.jpg")
    @NotNull(message = "이벤트 이미지 URL을 입력하세요.")
    @NotBlank(message = "이벤트 이미지 URL은 공백이나 빈 문자를 허용하지 않습니다.")
    private String picture;

    @Schema(description = "이벤트 간략 설명", example = "이 페스티벌은 다양한 활동과 공연이 포함되어 있습니다.")
    @NotNull(message = "이벤트 간략 설명을 입력하세요.")
    @NotBlank(message = "이벤트 간략 설명은 공백이나 빈 문자를 허용하지 않습니다.")
    private String shortDescription;

    @Schema(description = "이벤트 상세 설명", example = "페스티벌은 7월 20일부터 22일까지 진행됩니다.")
    @NotNull(message = "이벤트 상세 설명을 입력하세요.")
    @NotBlank(message = "이벤트 상세 설명은 공백이나 빈 문자를 허용하지 않습니다.")
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
