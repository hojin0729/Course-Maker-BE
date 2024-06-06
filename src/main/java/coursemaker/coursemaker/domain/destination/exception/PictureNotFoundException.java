package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class PictureNotFoundException extends RootException {

    public PictureNotFoundException(ErrorCode message, String logMessage) {
        super(message, logMessage);
    }
}
