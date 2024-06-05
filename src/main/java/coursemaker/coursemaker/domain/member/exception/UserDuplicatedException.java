package coursemaker.coursemaker.domain.member.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class UserDuplicatedException extends RootException {
    String message;

    public UserDuplicatedException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_MEMBER, logMessage);

        this.message = message;
    }
}
