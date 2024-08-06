package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class UnAuthorizedException extends RootException {

    private final String message;
    public UnAuthorizedException(String message, String logMessage) {
        super(ErrorCode.UNAUTHORIZED_MEMBER, logMessage, message);
        this.message = message;
    }
}
