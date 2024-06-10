package coursemaker.coursemaker.jwt.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class InvalidTokenException extends RootException {
    String message;

    public InvalidTokenException(String message, String logMessage) {
        super(ErrorCode.INVALID_TOKEN, logMessage);
        this.message = message;
    }
}
