package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class InvalidTokenException extends RootException {
    private final String message;

    public InvalidTokenException(String message, String logMessage) {
        super(ErrorCode.INVALID_TOKEN, logMessage, message);
        this.message = message;
    }
}
