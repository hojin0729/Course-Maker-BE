package coursemaker.coursemaker.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class JoinRequestDto {
    @Schema(description = "이메일", example = "user@example.com")
    @NotNull(message = "이메일을 입력하세요.")
    @NotBlank(message = "이메일은 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    @NotNull(message = "이름을 입력하세요.")
    @NotBlank(message = "이름은 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Pattern(regexp = "[가-힣]{2,10}", message = "이름 한글로 구성되어야 합니다.")
    private String name;

    @Schema(description = "닉네임", example = "nickname1")
    @NotNull(message = "닉네임을 입력하세요.")
    @NotBlank(message = "닉네임은 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Pattern(regexp = "[a-z0-9가-힣]{2,10}", message = "닉네임은 2~10자의 영문 소문자, 숫자, 한글로 구성되어야 합니다.")
    private String nickname;

    @Schema(description = "비밀번호", example = "password1@")
    @NotNull(message = "비밀번호를 입력하세요.")
    @NotBlank(message = "비밀번호는 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Size(min = 4, max = 15, message = "비밀번호는 최소 4자 이상, 최대 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$", message = "적어도 하나의 숫자/특수문자/소문자가 포함되어야 합니다.")
    private String password;

    @Schema(description = "전화번호", example = "010-1234-5678")
    @NotNull(message = "전화번호를 입력하세요.")
    @NotBlank(message = "전화번호는 공백 혹은 빈 문자는 허용하지 않습니다.")
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}", message = "유효하지 않은 전화번호 형식입니다.")
    private String phoneNumber;

//    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
//    private String profileImgUrl;
//
//    @Schema(description = "프로필 설명", example = "이것은 프로필 설명입니다.")
//    private String profileDescription;

}

