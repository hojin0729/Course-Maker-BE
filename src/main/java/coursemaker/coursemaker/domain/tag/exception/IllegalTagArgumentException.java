package coursemaker.coursemaker.domain.tag.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class IllegalTagArgumentException extends RootException {

    String message;

    public IllegalTagArgumentException(String message, String logMessage) {
        super(ErrorCode.ILLEGAL_TAG_ARGUMENT, logMessage);

        this.message = message;
    }
}
