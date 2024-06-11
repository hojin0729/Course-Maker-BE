package coursemaker.coursemaker.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCodeVerifyRequest {
    private String toEmail;
    private String emailCode;
}
