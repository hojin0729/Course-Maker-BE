package coursemaker.coursemaker.domain.course.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class TravelCourseDuplicatedException extends RootException {

    String message;

    public TravelCourseDuplicatedException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_COURSE, logMessage);

        this.message = message;
    }
}