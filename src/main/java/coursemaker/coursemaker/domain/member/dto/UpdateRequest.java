package coursemaker.coursemaker.domain.member.dto;

import coursemaker.coursemaker.domain.member.exception.IllegalUserArgumentException;
import lombok.Data;

@Data
public class UpdateRequest {
    private Long userId; //TODO: 쿠키에서 가져오기
    private String name;
    private String nickname;
    private String password;
    private String profileImgUrl;
    private String profileDescription;

    public void validate() {
        if (userId == null) {
            throw new IllegalUserArgumentException("유저 ID가 비어있습니다.", "userId is empty");
        }
        if (name != null && !name.isBlank() && !name.matches("[a-zA-Z가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("이름은 2~10자의 영문자, 한글로 구성되어야 합니다.", "invalid name format");
        }
        if (nickname != null && !nickname.isBlank() && !nickname.matches("[a-zA-Z가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("닉네임은 2~10자의 영문자, 한글로 구성되어야 합니다.", "invalid nickname format");
        }
        if (password != null && !password.isBlank() && (password.length() < 4 || password.length() > 15)) {
            throw new IllegalUserArgumentException("비밀번호는 최소 4자 이상 15자 이하이어야 합니다.", "password is too short");
        }
        if (profileImgUrl != null && !profileImgUrl.isBlank() && !profileImgUrl.matches("^(http|https)://.*$")) {
            throw new IllegalUserArgumentException("유효하지 않은 프로필 이미지 URL입니다.", "invalid profile image URL format");
        }
    }
}

