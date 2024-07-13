package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class InvalidTokenException extends RootException {
    public InvalidTokenException(String message, String logMessage) {
        super(ErrorCode.INVALID_TOKEN, logMessage, message);
    }
}
