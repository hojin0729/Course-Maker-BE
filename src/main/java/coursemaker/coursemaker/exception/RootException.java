package coursemaker.coursemaker.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RootException extends RuntimeException{
    private ErrorCode errorCode;
    private String logMessage;

    public RootException(ErrorCode errorCode, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.logMessage = logMessage;
        log.error("exception code: {}, message: {}", errorCode.getCode(), logMessage);
    }


}
