package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class CourseReviewNotFoundException extends RootException {
    public CourseReviewNotFoundException(String message) {
        super(ErrorCode.INVALID_REVIEW, message);
    }
}
