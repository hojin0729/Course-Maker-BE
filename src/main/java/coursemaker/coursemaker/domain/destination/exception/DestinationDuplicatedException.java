package coursemaker.coursemaker.domain.destination.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class DestinationDuplicatedException extends RootException {
    String message;

    public DestinationDuplicatedException(String message, String logMessage) {
        super(ErrorCode.DUPLICATED_DESTINATION, logMessage);

        this.message = message;
    }
}
