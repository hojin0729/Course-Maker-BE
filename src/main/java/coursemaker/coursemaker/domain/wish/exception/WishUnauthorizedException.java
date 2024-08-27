package coursemaker.coursemaker.domain.wish.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class WishUnauthorizedException extends RootException {
    String message;

    public WishUnauthorizedException(String message, String logMessage) {
        super(ErrorCode.WISH_UNAUTHORIZED, logMessage);

        this.message = message;
    }
}
