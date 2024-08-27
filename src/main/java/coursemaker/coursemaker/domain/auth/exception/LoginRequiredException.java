package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class LoginRequiredException extends RootException {
    private String message;

    public LoginRequiredException(String message, String logMessage) {
        super(ErrorCode.LOGIN_REQUIRED, logMessage, message);
        this.message = message;
    }
}
