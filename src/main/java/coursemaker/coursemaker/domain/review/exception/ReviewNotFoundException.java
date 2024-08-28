package coursemaker.coursemaker.domain.review.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class ReviewNotFoundException extends RootException {
    private String message;

    public ReviewNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_REVIEW, logMessage, message);
        this.message = message;
    }
}
