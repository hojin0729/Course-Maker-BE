package coursemaker.coursemaker.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerifyResponse {
    Boolean isSuccess;
    String status;
}
