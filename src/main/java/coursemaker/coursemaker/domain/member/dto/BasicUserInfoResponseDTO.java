package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasicUserInfoResponseDTO {
    @Schema(description = "닉네임", example = "nickname12")
    String nickname;

    @Schema(description = "이름", example = "홍길동")
    String name;

    @Schema(description = "여행자 등급", example = "초보 여행가")
    String role;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    String profileImgUrl;
}
