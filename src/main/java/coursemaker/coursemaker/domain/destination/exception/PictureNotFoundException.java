package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class PictureNotFoundException extends RootException {
    String message;

    public PictureNotFoundException(String message, String logMessage) {
        super(ErrorCode.PICTURE_NOT_FOUND, logMessage);

        this.message = message;
    }
}
