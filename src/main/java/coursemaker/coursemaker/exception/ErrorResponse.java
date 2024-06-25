package coursemaker.coursemaker.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ErrorResponse {
    @Schema(description = "상태코드", example = "404")
    private Integer status;

    @Schema(description = "에러 종류", example = "Invalid item")
    private String errorType;

    @Schema(description = "에러 메세지", example = "해당하는 태그가 없습니다.")
    private String message;
}
