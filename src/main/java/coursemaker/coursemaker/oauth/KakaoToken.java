package coursemaker.coursemaker.oauth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kakao_token", indexes = {
        @Index(name = "idx_access_token", columnList = "accessToken")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoToken;

    private String accessToken;
}