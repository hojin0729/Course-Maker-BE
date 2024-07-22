package coursemaker.coursemaker.domain.notice.dto;

import coursemaker.coursemaker.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseNoticeDto {
    @Schema(hidden = true)
    private Long id;
    private String title;
    private String picture;
    private String description;

    public static ResponseNoticeDto toDto(Notice notice) {
        ResponseNoticeDto dto = new ResponseNoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setPicture(notice.getPicture());
        dto.setDescription(notice.getDescription());
        return dto;
    }
}
