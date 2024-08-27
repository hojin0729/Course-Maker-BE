package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class IllegalReviewArgumentException extends RootException {

    private String message;
    public IllegalReviewArgumentException(String message, String logMessage) {
        super(ErrorCode.ILLEGAL_REVIEW_ARGUMENT, logMessage, message);
        this.message = message;
    }
}
