package coursemaker.coursemaker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
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
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "Authentication failed", "비밀번호가 잘못되었습니다.", "MEMBER-002"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "Invalid item", "회원 정보가 없습니다.", "MEMBER-003"),
    ILLEGAL_MEMBER_ARGUMENT(HttpStatus.CONFLICT, "Illegal argument", "회원에 알맞은 인자값이 아닙니다.", "MEMBER-004"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "Authorization failed", "인증된 회원이 아닙니다.", "MEMBER-005"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Illegal argument", "토큰 형식이 잘못되었습니다.", "MEMBER-006");



    private String description;
    private HttpStatus status;
    private String code;
    private String errorType;

    ErrorCode(HttpStatus status, String errorType, String description, String code) {
        this.status = status;
        this.errorType = errorType;
        this.description = description;
        this.code = code;
    }
}