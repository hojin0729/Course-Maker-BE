package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DestinationNotFoundException extends RootException {
    String message;

    public DestinationNotFoundException(String message, String logMessage) {
        super(ErrorCode.INVALID_DESTINATION, logMessage);

        this.message = message;
    }
}
