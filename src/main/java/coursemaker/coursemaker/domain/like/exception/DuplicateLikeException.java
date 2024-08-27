package coursemaker.coursemaker.domain.like.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DuplicateLikeException extends RootException {
    String message;

    public DuplicateLikeException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_LIKE, logMessage, message);

        this.message = message;
    }
}