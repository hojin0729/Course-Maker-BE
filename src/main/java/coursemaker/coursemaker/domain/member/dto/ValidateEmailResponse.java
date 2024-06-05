package coursemaker.coursemaker.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateEmailResponse {
    String fromMail;
    String toMail;
    String title;
    String authCode;
    Boolean isDuplicate;
    Boolean isInappropriate; //조건에 부적절할 경우 true
}

