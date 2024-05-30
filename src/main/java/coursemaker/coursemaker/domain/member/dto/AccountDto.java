package coursemaker.coursemaker.domain.member.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    @NotBlank(message = "아이디를 입력하세요")
    private String username; //회원 id

    @NotBlank(message = "이름을 입력하세요")
    private String name; //회원 이름

    @NotBlank(message = "이메일을 입력하세요")
    private String email; // 회원 이메일
    private String nickname; //회원 닉네임
    private String phoneNumber; //회원 전화번호
    private String password; //회원 비밀번호
    private String roles; //회원 등급
}
