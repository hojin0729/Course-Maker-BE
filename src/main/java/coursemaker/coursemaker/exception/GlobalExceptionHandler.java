package coursemaker.coursemaker.exception;

import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseAlreadyDeletedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.destination.exception.*;
import coursemaker.coursemaker.domain.member.exception.*;
import coursemaker.coursemaker.domain.tag.exception.IllegalTagArgumentException;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("login required");
        response.setMessage("로그인 후 이용이 가능합니다.");
        response.setStatus(401);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("Illegal argument");
        response.setMessage("인자값이 잘못됬습니다: "+e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> handleRootException(RootException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("unknown error");
        response.setMessage("예상치 못한 오류가 발생했습니다: "+e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        ErrorResponse response = new ErrorResponse();

        response.setErrorType("unknown error");
        response.setMessage("예상치 못한 쌈@뽕한 오류가 발생했습니다: " + e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse response = new ErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            response.setMessage(errorMessage);
            response.setStatus(400);
            response.setErrorType("Illegal argument");
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Member 관련 예외처리
    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(IllegalUserArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalUserArgumentException(IllegalUserArgumentException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()

                .value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Course 관련 예외처리
    @ExceptionHandler(TravelCourseDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleTagDuplicatedException(TravelCourseNotFoundException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(TravelCourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TravelCourseNotFoundException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalTravelCourseArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTravelCourseArgumentException(IllegalTravelCourseArgumentException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(TravelCourseAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleTravelCourseAlreadyDeletedException(TravelCourseAlreadyDeletedException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // Destination 관련 예외 처리
    @ExceptionHandler(DestinationDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleDestinationDuplicatedException(DestinationDuplicatedException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(DestinationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDestinationNotFoundException(DestinationNotFoundException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(IllegalDestinationArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalDestinationArgumentException(IllegalDestinationArgumentException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePictureNotFoundException(PictureNotFoundException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Tag 관련 예외처리
    @ExceptionHandler(TagDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleTagDuplicatedException(TagDuplicatedException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TagNotFoundException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalTagArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTagArgumentException(IllegalTagArgumentException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 403 포비든 에러 (공용) 예외처리
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // S3 관련 예외처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ErrorCode.PICTURE_OVER_SIZE.getErrorType());
        response.setMessage(ErrorCode.PICTURE_OVER_SIZE.getDescription());
        response.setStatus(ErrorCode.PICTURE_OVER_SIZE.getStatus().value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
