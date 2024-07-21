package coursemaker.coursemaker.domain.notice.dto;

import coursemaker.coursemaker.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestNoticeDto {
    @Schema(hidden = true)
    private Long id;
    private String title;
    private String picture;
    private String description;

    public Notice toEntity(){
        Notice notice = new Notice();
        notice.setId(id);
        notice.setTitle(title);
        notice.setPicture(picture);
        notice.setDescription(description);
        return notice;
    }


}
