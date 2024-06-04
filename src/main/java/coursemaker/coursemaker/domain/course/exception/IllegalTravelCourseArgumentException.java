package coursemaker.coursemaker.domain.course.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class IllegalTravelCourseArgumentException extends RootException {

    String message;

    public IllegalTravelCourseArgumentException(String message, String logMessage) {
        super(ErrorCode.ILLEGAL_TAG_ARGUMENT, logMessage);

        this.message = message;
    }
}
