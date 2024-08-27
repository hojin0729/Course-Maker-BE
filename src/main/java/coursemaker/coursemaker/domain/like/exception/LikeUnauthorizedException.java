package coursemaker.coursemaker.domain.like.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class LikeUnauthorizedException extends RootException {
    String message;

    public LikeUnauthorizedException(String message, String logMessage) {
        super(ErrorCode.UNAUTHORIZED_LIKE, logMessage);

        this.message = message;
    }
}
