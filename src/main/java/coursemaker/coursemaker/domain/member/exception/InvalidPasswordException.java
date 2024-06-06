package coursemaker.coursemaker.domain.member.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RootException {
    String message;
    public InvalidPasswordException(String message, String logMessage) {
        super(ErrorCode.UNAUTHORIZED_MEMBER, logMessage);

        this.message = message;
    }
}
