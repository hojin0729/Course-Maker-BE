package coursemaker.coursemaker.domain.course.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class TravelCourseNotFoundException extends RootException {
    String message;

    public TravelCourseNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_COURSE, logMessage);

        this.message = message;
    }
}