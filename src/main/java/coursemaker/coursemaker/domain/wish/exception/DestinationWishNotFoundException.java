package coursemaker.coursemaker.domain.wish.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DestinationWishNotFoundException extends RootException {
    String message;

    public DestinationWishNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_WISH, logMessage, message);
        this.message = message;
    }
}