package coursemaker.coursemaker.domain.like.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DestinationLikeNotFoundException extends RootException {
    String message;

    public DestinationLikeNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_LIKE, logMessage, message);
        this.message = message;
    }
}