package coursemaker.coursemaker.domain.like.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class CourseLikeNotFoundException extends RootException {
    String message;


    public CourseLikeNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_LIKE, logMessage, message);
        this.message = message;
    }
}