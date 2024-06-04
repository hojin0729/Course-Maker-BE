package coursemaker.coursemaker.domain.member.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginEmail;
    private String password;
}
