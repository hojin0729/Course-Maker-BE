package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class ExpiredTokenException extends RootException {
    public ExpiredTokenException(String message, String logMessage) {
        super(ErrorCode.EXPIRED_TOKEN, logMessage, message);
    }
}
