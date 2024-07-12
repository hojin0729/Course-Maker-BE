package coursemaker.coursemaker.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RootException extends RuntimeException{
    private ErrorCode errorCode;
    private String logMessage;
    private String clientMessage;

    public RootException(ErrorCode errorCode, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.logMessage = logMessage;
        log.error("exception code: {}, message: {}", errorCode.getCode(), logMessage);
    }

    public RootException(ErrorCode errorCode, String logMessage, String clientMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
        log.error("exception code: {}, message: {}", errorCode.getCode(), logMessage);
    }


}
