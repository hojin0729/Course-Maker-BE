package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RootException {
    private final String message;
    public InvalidPasswordException(String message, String logMessage) {
        super(ErrorCode.WRONG_PASSWORD, logMessage, message);

        this.message = message;
    }
}
