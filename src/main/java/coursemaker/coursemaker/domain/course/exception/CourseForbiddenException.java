package coursemaker.coursemaker.domain.course.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class CourseForbiddenException extends RootException {

    String message;

    public CourseForbiddenException(String message, String logMessage) {
        super(ErrorCode.COURSE_FORBIDDEN, logMessage);

        this.message = message;
    }
}