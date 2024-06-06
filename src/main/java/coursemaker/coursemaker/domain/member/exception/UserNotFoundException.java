package coursemaker.coursemaker.domain.member.exception;

import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.RootException;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RootException {
    String message;

   public UserNotFoundException(String message, String logMessage) {
       super(ErrorCode.NOT_FOUND_MEMBER, logMessage);
       this.message = message;
   }
}
