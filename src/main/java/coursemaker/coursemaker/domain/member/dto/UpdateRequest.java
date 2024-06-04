package coursemaker.coursemaker.domain.member.dto;

import lombok.Data;

@Data
public class UpdateRequest {
    private Long userId; //TODO: 쿠키에서 가져오기
    private String name;
    private String nickname;
    private String password;
    private String profileImgUrl;
    private String profileDescription;
}

