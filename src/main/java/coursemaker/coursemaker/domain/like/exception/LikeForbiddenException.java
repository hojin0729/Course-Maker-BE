package coursemaker.coursemaker.domain.like.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class LikeForbiddenException extends RootException {
    String message;

    public LikeForbiddenException(String message, String logMessage) {
        super(ErrorCode.FORBIDDEN_LIKE, logMessage, message);

        this.message = message;
    }
}
