package coursemaker.coursemaker.domain.member.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class UnauthorizedException extends RootException {

    String message;
    public UnauthorizedException(String message, String logMessage) {
        super(ErrorCode.UNAUTHORIZED_MEMBER, logMessage);
    }
}
