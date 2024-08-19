package coursemaker.coursemaker.domain.course.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class TravelCourseAlreadyDeletedException extends RootException {

    String message;

    public TravelCourseAlreadyDeletedException(String message, String logMessage) {
        super(ErrorCode.ALREADY_DELETED_COURSE, logMessage);

        this.message = message;
    }
}