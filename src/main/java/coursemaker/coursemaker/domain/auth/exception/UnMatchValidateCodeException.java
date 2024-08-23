package coursemaker.coursemaker.domain.auth.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class UnMatchValidateCodeException extends RootException {

    private final String message;
    public UnMatchValidateCodeException(String message, String logMessage){
        super(ErrorCode.MISMATCH_CODE, logMessage, message);
        this.message = message;
    }
}
