package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class ExpiredAccessTokenException extends RootException {
    private final String message;

    public ExpiredAccessTokenException(String message, String logMessage) {
        super(ErrorCode.EXPIRED_ACCESS_TOKEN, logMessage, message);
        this.message = message;
    }
}
