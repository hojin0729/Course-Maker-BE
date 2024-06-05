//package coursemaker.coursemaker.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService {
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    //TODO : 토큰저장 로직
////    public void saveTokenInfo(Long userId, String refreshToken, String accessToken, int remainingTTL) {
////        refreshTokenRepository.save(new RefreshToken(String.valueOf(userId), refreshToken, accessToken, remainingTTL));
////    }
//
//    public void removeTokenInfo(String accessToken) {
//        refreshTokenRepository.findByAccessToken(accessToken)
//                .ifPresent(refreshTokenRepository::delete);
//    }
//}
