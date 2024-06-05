package coursemaker.coursemaker.domain.member.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String profileImgUrl;
    private String profileDescription;
}

