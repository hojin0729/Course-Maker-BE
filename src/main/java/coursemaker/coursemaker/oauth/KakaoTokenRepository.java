package coursemaker.coursemaker.oauth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, String> {
    Optional<KakaoToken> findByAccessToken(String accessToken);
}

