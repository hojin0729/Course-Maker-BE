package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class UnSendEmailException extends RootException {

    private final String message;
    public UnSendEmailException(String message, String logMessage){
        super(ErrorCode.UNSEND_EMAIL, logMessage, message);
        this.message = message;
    }
}
