package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class ExpiredRefreshTokenException extends RootException {
    private final String message;

    public ExpiredRefreshTokenException(String message, String logMessage) {
        super(ErrorCode.EXPIRED_REFRESH_TOKEN, logMessage, message);
        this.message = message;
    }
}
