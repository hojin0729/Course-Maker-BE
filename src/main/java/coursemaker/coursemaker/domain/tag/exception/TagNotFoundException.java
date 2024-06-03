package coursemaker.coursemaker.domain.tag.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class TagNotFoundException extends RootException {
    String message;

    public TagNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_TAG, logMessage);

        this.message = message;
    }
}
