package coursemaker.coursemaker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*Tag 도메인 예외*/
    INVALID_TAG(HttpStatus.NOT_FOUND, "해당하는 태그가 없습니다.", "TAG-001"),
    DUPLICATED_TAG(HttpStatus.CONFLICT, "해당 태그가 이미 존재합니다.", "TAG-002"),

    /*Destination 도메인 예외*/
    INVALID_DESTINATION(HttpStatus.NOT_FOUND, "해당하는 여행지가 없습니다.", "DEST-001"),
    DUPLICATED_DESTINATION(HttpStatus.CONFLICT, "해당 여행지가 이미 존재합니다.", "DEST-002"),

    /*Course 도메인 예외*/
    INVALID_COURSE(HttpStatus.NOT_FOUND, "해당하는 코스가 없습니다.", "COURSE-001"),
    DUPLICATED_COURSE(HttpStatus.CONFLICT, "해당 코스가 이미 존재합니다.", "COURSE-002");


    private String description;
    private HttpStatus  status;
    private String code;

    ErrorCode(HttpStatus status, String description, String code) {
        this.status = status;
        this.description = description;
        this.code = code;
    }
}
