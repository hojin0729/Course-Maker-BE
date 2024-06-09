package coursemaker.coursemaker.domain.member.dto;

import coursemaker.coursemaker.domain.member.exception.IllegalUserArgumentException;
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

    public void validate() {
        if (email == null || email.isBlank()) {
            throw new IllegalUserArgumentException("이메일 입력값이 비어있습니다.", "email is empty");
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalUserArgumentException("유효하지 않은 이메일 형식입니다.", "invalid email format");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalUserArgumentException("이름 입력값이 비어있습니다.", "name is empty");
        }
        if (!name.matches("[a-zA-Z가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("이름은 2~10자의 영문자, 한글로 구성되어야 합니다.", "invalid name format");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalUserArgumentException("닉네임 입력값이 비어있습니다.", "nickname is empty");
        }
        if (!nickname.matches("[a-zA-Z가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("닉네임은 2~10자의 영문자, 한글로 구성되어야 합니다.", "invalid nickname format");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalUserArgumentException("비밀번호 입력값이 비어있습니다.", "password is empty");
        }
        if (password.length() < 4 || password.length() > 15) {
            throw new IllegalUserArgumentException("비밀번호는 최소 4자 이상, 최대 15자 이하이어야 합니다.", "password length is invalid");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalUserArgumentException("전화번호 입력값이 비어있습니다.", "phone number is empty");
        }
        if (!phoneNumber.matches("\\d{3}-\\d{3,4}-\\d{4}")) {
            throw new IllegalUserArgumentException("유효하지 않은 전화번호 형식입니다.", "invalid phone number format");
        }
        if (profileImgUrl != null && !profileImgUrl.isBlank() && !profileImgUrl.matches("^(http|https)://.*$")) {
            throw new IllegalUserArgumentException("유효하지 않은 프로필 이미지 URL입니다.", "invalid profile image URL format");
        }
    }
}

