package coursemaker.coursemaker.domain.auth.dto;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String refreshToken;
}
