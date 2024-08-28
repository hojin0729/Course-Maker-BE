package coursemaker.coursemaker.domain.wish.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DuplicateWishException extends RootException {
    String message;

    public DuplicateWishException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_WISH, logMessage, message);

        this.message = message;
    }
}