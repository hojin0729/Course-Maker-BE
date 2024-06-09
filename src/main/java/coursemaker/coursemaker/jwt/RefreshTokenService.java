package coursemaker.coursemaker.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    //TODO : 토큰저장 로직
    public void saveTokenInfo(Long userId, String refreshToken, String accessToken, int remainingTTL) {
        RefreshToken refreshTokenInfo = new RefreshToken(userId.toString(), refreshToken, accessToken, remainingTTL, false);
        refreshTokenRepository.save(refreshTokenInfo);
    }
    
//    /*블랙리스트 지정방식*/
//    public void setBlackList(String accessToken) {
//        if(refreshTokenRepository.findByAccessToken(accessToken).isPresent()) {
//            RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken).get();
//            token.setBlackList();
//            refreshTokenRepository.save(token);
//        }
//    }
//    public Boolean isBlackList(String accessToken) {
//        if(refreshTokenRepository.findByAccessToken(accessToken).isPresent()) {
//            RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken).get();
//            return token.getIsBlackListed();
//        }
//        return true;
//    }

    public void removeTokenInfo(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
