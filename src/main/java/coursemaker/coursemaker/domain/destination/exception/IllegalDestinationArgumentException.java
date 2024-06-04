package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class IllegalDestinationArgumentException extends RootException {
    String message;

    public IllegalDestinationArgumentException(String message, String logMessage) {
        super(ErrorCode.ILLEGAL_DESTINATION_ARGUMENT, logMessage);

        this.message = message;
    }
}
