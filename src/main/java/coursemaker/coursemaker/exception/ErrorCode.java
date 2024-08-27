package coursemaker.coursemaker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "Unknown error", "예상치 못한 오류가 발생했습니다.", "UNKNOWN-001"),

    /*Tag 도메인 예외*/
    INVALID_TAG(HttpStatus.NOT_FOUND, "Invalid item", "해당하는 태그가 없습니다.", "TAG-001"),
    DUPLICATED_TAG(HttpStatus.CONFLICT, "Duplicated item", "해당 태그가 이미 존재합니다.", "TAG-002"),
    ILLEGAL_TAG_ARGUMENT(HttpStatus.BAD_REQUEST, "Illegal argument", "태그에 알맞은 인자값이 아닙니다.", "TAG-003"),

    /*Destination 도메인 예외*/
    INVALID_DESTINATION(HttpStatus.NOT_FOUND, "Invalid item", "해당하는 여행지가 없습니다.", "DEST-001"),
    DUPLICATED_DESTINATION(HttpStatus.CONFLICT, "Duplicated item", "해당 여행지가 이미 존재합니다.", "DEST-002"),
    ILLEGAL_DESTINATION_ARGUMENT(HttpStatus.BAD_REQUEST, "Illegal argument", "여행지에 알맞은 인자값이 아닙니다.", "DEST-003"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden", "해당 자원에 접근할 권한이 없습니다.", "DEST-004"),

    PICTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "Invalid item", "해당하는 사진이 없습니다.", "PICTURE-001"),
    PICTURE_OVER_SIZE(HttpStatus.PAYLOAD_TOO_LARGE, "Out of range size", "허용하는 사진 용량(15MB)을 초과했습니다.", "PICTURE-002"),

    /*Course 도메인 예외*/
    INVALID_COURSE(HttpStatus.NOT_FOUND, "Invalid item", "해당하는 코스가 없습니다.", "COURSE-001"),
    DUPLICATED_COURSE(HttpStatus.CONFLICT, "Duplicated item", "해당 코스가 이미 존재합니다.", "COURSE-002"),
    ILLEGAL_COURSE_ARGUMENT(HttpStatus.BAD_REQUEST, "Illegal argument", "코스에 알맞은 인자값이 아닙니다.", "COURSE-003"),
    ALREADY_DELETED_COURSE(HttpStatus.CONFLICT, "Already deleted", "해당 코스는 이미 삭제되었습니다.", "COURSE-004"),
    COURSE_FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden", "해당 코스에 접근할 권한이 없습니다.", "COURSE-005"),

    /*Member 도메인 예외*/
    /* 회원가입, 로그인 시 */
    DUPLICATED_MEMBER(HttpStatus.CONFLICT, "Duplicated item", "이미 존재하는 아이디입니다.", "MEMBER-001"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "Invalid item", "회원 정보가 없습니다.", "MEMBER-002"),
    ILLEGAL_MEMBER_ARGUMENT(HttpStatus.CONFLICT, "Illegal argument", "회원에 알맞은 인자값이 아닙니다.", "MEMBER-003"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "Authorization failed", "인증된 회원이 아닙니다.", "MEMBER-004"),

    /*Auth 도메인 예와*/
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "Authentication failed", "비밀번호가 잘못되었습니다.", "AUTH-001"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Invalid token", "토큰 형식이 잘못되었습니다.", "AUTH-002"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token", "Access 토큰이 만료됬습니다.", "AUTH-003"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token", "Refresh 토큰이 만료됬습니다.", "AUTH-004"),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "login required", "로그인 후 이용이 가능합니다.", "AUTH-005"),

    MISMATCH_EMAIL(HttpStatus.NOT_FOUND, "Invalid item", "이메일 전송 기록이 없습니다.", "AUTH-005"),
    MISMATCH_CODE(HttpStatus.BAD_REQUEST, "Mismatch code", "검증 코드가 일치하지 않습니다.", "AUTH-006"),

    TIME_OUT(HttpStatus.BAD_REQUEST, "Timeout", "유효 시간이 지났습니다.", "TIME-OUT"),

    /* Review 도메인 예외 */
    INVALID_REVIEW(HttpStatus.NOT_FOUND, "Invalid item", "해당하는 리뷰가 없습니다.", "REVIEW-001"),
    DUPLICATED_REVIEW(HttpStatus.CONFLICT, "Duplicated item", "해당 리뷰가 이미 존재합니다.", "REVIEW-002"),
    ILLEGAL_REVIEW_ARGUMENT(HttpStatus.BAD_REQUEST, "Illegal argument", "리뷰에 알맞은 인자값이 아닙니다.", "REVIEW-003"),

    /* Wish 찜하기 도매인 예외*/
    INVALID_WISH(HttpStatus.NOT_FOUND, "Invalid wish", "해당하는 찜이 없습니다.", "WISH-001"),
    UNAUTHORIZED_WISH(HttpStatus.UNAUTHORIZED, "Unauthorized", "찜하기 기능에 접근할 권한이 없습니다.", "WISH-002"),
    FORBIDDEN_WISH(HttpStatus.CONFLICT, "Forbidden", "찜하기 기능에 접근할 권한이 없습니다.", "WISH-003"),
    DUPLICATED_WISH(HttpStatus.CONFLICT, "Duplicated wish", "이미 찜한 소스입니다.", "WISH-004");





    private final String description;
    private final HttpStatus status;
    private final String code;
    private final String errorType;

    ErrorCode(HttpStatus status, String errorType, String description, String code) {
        this.status = status;
        this.errorType = errorType;
        this.description = description;
        this.code = code;
    }
}