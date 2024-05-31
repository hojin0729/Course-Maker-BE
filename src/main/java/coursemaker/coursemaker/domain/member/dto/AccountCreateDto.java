package coursemaker.coursemaker.domain.member.dto;

import lombok.*;

@Getter
@Setter
public class AccountCreateDto {
    private String username;
    private String name;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String password;
    private String roles;
}
