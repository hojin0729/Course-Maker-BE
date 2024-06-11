package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class ForbiddenException extends RootException {
    String message;

    public ForbiddenException(String message, String logMessage) {
        super(ErrorCode.FORBIDDEN, logMessage);

        this.message = message;
    }
}
