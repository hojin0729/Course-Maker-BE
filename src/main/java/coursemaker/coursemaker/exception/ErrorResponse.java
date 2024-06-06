package coursemaker.coursemaker.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer status;
    private String errorType;
    private String message;
}
