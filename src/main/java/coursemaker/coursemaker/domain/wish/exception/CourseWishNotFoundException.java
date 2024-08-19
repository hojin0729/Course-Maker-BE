package coursemaker.coursemaker.domain.wish.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class CourseWishNotFoundException extends RootException {
    String message;


    public CourseWishNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_WISH, logMessage, message);
        this.message = message;
    }
}