package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class IllegalCourseReviewArgumentException extends RootException {
    public IllegalCourseReviewArgumentException(String message) {
        super(ErrorCode.ILLEGAL_REVIEW_ARGUMENT, message);
    }
}
