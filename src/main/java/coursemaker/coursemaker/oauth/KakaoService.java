package coursemaker.coursemaker.oauth;

import coursemaker.coursemaker.domain.member.dto.LoginResponse;
import coursemaker.coursemaker.domain.member.dto.LogoutResponse;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.jwt.JwtTokenProvider;
import coursemaker.coursemaker.jwt.JwtUtil;
import coursemaker.coursemaker.jwt.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final KakaoTokenService kakaoTokenService;
    private final JwtUtil jwtUtil;
    private final KakaoOauth kakaoOauth;
    private final MemberService userService;

    public void kakaoSignUp(String kakaoUserId, String kakaoNickname){
        log.info("카카오 회원가입 시작");
        String randomPassword = String.valueOf(UUID.randomUUID()).substring(0,8);
        String generatedNickname = kakaoNickname + "#" + generateRandomPostfix();

        Member builtUser = Member.addMemberBuilder()
                .email(kakaoUserId+"@coursemaker.com")
                .loginType(Member.LoginType.KAKAO)
                .name(kakaoNickname)
                .nickname(generatedNickname)
                .password(randomPassword)
                .profileImgUrl("")
                .profileDescription("자기소개해주세요")
                .roles("ROLE_USER")
                .build();

        memberRepository.save(builtUser);
    }

    public LoginResponse kakaoLogin(String kakaoUserId, HttpServletResponse response, String kakaoToken){
        Member loginUser = memberRepository.findByEmail(kakaoUserId+"@coursemaker.com")
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다.", "User ID: " + kakaoUserId));

        String accessToken = jwtTokenProvider.createAccessTokenKakao(loginUser.getId(), kakaoToken, loginUser.getLoginType());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        log.info("[getLogInResult] LogInResponse 객체에 값 주입");
        response.addHeader("Authorization", "Bearer " + accessToken);

        refreshTokenService.saveTokenInfo(loginUser.getId(), refreshToken, accessToken, 60 * 60 * 24 * 7);
        kakaoTokenService.saveKakaoTokenInfo(loginUser.getId(), kakaoToken, accessToken);

        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, token : {}", loginUser.getId(), loginResponse.getAccessToken());
        return loginResponse;
    }

    public LogoutResponse kakaoLogout(HttpServletRequest request) {
        String accessToken = jwtUtil.getTokenFromRequest(request);
        String kakaoToken = jwtTokenProvider.getKakaoToken(accessToken);
        kakaoOauth.expireKakaoToken(kakaoToken);
        kakaoTokenService.removeKakaoTokenInfo(accessToken);
        userService.logout(request);

        return LogoutResponse.builder().success(true).build();
    }

    public String generateRandomPostfix() {
        int leftLimit = 48; // 숫자 '0'의 ASCII 코드
        int rightLimit = 122; // 알파벳 'z'의 ASCII 코드
        int stringLength = 4;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1) // leftLimit(포함) 부터 rightLimit+1(불포함) 사이의 난수 스트림 생성
                .filter(i -> (i < 57 || i >= 65) && ( i <= 90 || i >= 97)) // ASCII 테이블에서 숫자, 대문자, 소문자만 사용함
                .limit(stringLength) // 생성된 난수를 지정된 길이로 잘라냄
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append) // 생성된 난수를 ASCII 테이블에서 대응되는 문자로 변환
                .toString(); // StringBuilder 객체를 문자열로 변환해 반환
    }
}
