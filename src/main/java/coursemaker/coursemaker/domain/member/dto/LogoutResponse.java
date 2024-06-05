package coursemaker.coursemaker.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse {
    Boolean success;
}

