package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DuplicatedReviewException extends RootException {
    private String message;
    public DuplicatedReviewException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_REVIEW, logMessage, message);
        this.message = message;
    }
}
