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
}

