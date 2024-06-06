package coursemaker.coursemaker.domain.member.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class IllegalUserArgumentException  extends RootException {
    String message;

    public IllegalUserArgumentException(String message, String logMessage) {
        super(ErrorCode.ILLEGAL_MEMBER_ARGUMENT, logMessage);

        this.message = message;
    }
}
