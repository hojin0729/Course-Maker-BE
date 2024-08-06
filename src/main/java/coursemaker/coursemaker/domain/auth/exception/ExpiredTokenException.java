package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class ExpiredTokenException extends RootException {
    private final String message;

    public ExpiredTokenException(String message, String logMessage) {
        super(ErrorCode.EXPIRED_TOKEN, logMessage, message);
        this.message = message;
    }
}
