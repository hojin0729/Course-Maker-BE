package coursemaker.coursemaker.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RootException extends RuntimeException{
    private ErrorCode errorCode;
    private String logMessage;
    private String clientMessage;
    private Exception e;

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

    public RootException(ErrorCode errorCode, String logMessage, String clientMessage, Exception e) {
        super(logMessage);
        this.errorCode = errorCode;
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
        this.e = e;
        log.error("exception code: {}, message: {}", errorCode.getCode(), logMessage);
        log.error("- exception trace - name: {}, cause: {}, message: {}", e.getClass().getName(), e.getCause(), e.getMessage());
    }


}
