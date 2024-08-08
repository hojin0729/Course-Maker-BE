package coursemaker.coursemaker.domain.auth.dto;

import coursemaker.coursemaker.domain.member.entity.Role;
import lombok.Data;

@Data
public class LoginedInfo {
    private String nickname;
    private Role role;
}
