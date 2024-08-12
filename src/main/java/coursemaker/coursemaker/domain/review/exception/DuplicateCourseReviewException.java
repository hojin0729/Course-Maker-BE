package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;

public class DuplicateCourseReviewException extends RootException {
    public DuplicateCourseReviewException(String message) {
        super(ErrorCode.DUPLICATED_REVIEW, message);
    }
}
