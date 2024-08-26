package coursemaker.coursemaker.domain.notice.dto;

import coursemaker.coursemaker.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseNoticeDto {
    @Schema(hidden = true)
    private Long id;
    @Schema(description = "공지사항 제목", example = "새로운 업데이트 알림")
    @NotNull(message = "공지사항 제목을 입력하세요.")
    @NotBlank(message = "공지사항 제목은 공백이나 빈 문자를 허용하지 않습니다.")
    private String title;
    @Schema(description = "공지사항 이미지 URL", example = "http://example.com/notice.jpg")
    @NotNull(message = "공지사항 이미지 URL을 입력하세요.")
    @NotBlank(message = "공지사항 이미지 URL은 공백이나 빈 문자를 허용하지 않습니다.")
    private String picture;
    @Schema(description = "공지사항 내용", example = "이번 업데이트에서는 다양한 기능이 추가되었습니다.")
    @NotNull(message = "공지사항 내용을 입력하세요.")
    @NotBlank(message = "공지사항 내용은 공백이나 빈 문자를 허용하지 않습니다.")
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
