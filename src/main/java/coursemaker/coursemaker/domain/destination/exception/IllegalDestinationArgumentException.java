package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class IllegalDestinationArgumentException extends RootException {

    public IllegalDestinationArgumentException(ErrorCode message, String logMessage) {
        super(message, logMessage);
    }
}
