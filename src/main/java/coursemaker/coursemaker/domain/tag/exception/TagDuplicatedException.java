package coursemaker.coursemaker.domain.tag.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class TagDuplicatedException extends RootException {

    String message;

    public TagDuplicatedException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_TAG, logMessage);

        this.message = message;
    }
}
