package coursemaker.coursemaker.domain.wish.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class WishForbiddenException extends RootException {
    String message;

    public WishForbiddenException(String message, String logMessage) {
        super(ErrorCode.WISH_FORBIDDEN, logMessage, message);

        this.message = message;
    }
}
