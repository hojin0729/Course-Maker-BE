package coursemaker.coursemaker.oauth;

import coursemaker.coursemaker.domain.member.dto.LoginResponse;
import coursemaker.coursemaker.domain.member.dto.LogoutResponse;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.jwt.JwtTokenProvider;
import coursemaker.coursemaker.jwt.JwtUtil;
import coursemaker.coursemaker.jwt.RefreshTokenService;
import coursemaker.coursemaker.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
    private final MemberService memberService;

    public void kakaoSignUp(String kakaoUserId, String kakaoNickname){
        log.info("카카오 회원가입 시작");
        if (memberRepository.findByEmail(kakaoUserId + "@coursemaker.com").isPresent()) {
            log.info("이미 존재하는 회원입니다: {}", kakaoUserId);
            return;
        }
        String randomPassword = String.valueOf(UUID.randomUUID()).substring(0,8);
        String generatedNickname = kakaoNickname;

        if (memberRepository.findByNickname(kakaoNickname).isPresent()) {
            throw new UserDuplicatedException("이미 존재하는 닉네임 입니다. ", "닉네임: " + kakaoNickname);
        }

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

        //만약 회원의 닉네임이 없거나 비어있다면, 카카오로부터 닉네임을 가져와서 업데이트하고 저장
        if (loginUser.getNickname() == null || loginUser.getNickname().isEmpty()) {
            Map<String, Object> userInfo = kakaoOauth.getUserInfoFromKakaoToken(kakaoToken);
            String nickname = (String) userInfo.get("nickname");
            loginUser.setNickname(nickname);
            memberRepository.save(loginUser);
        }

        String accessToken = jwtTokenProvider.createAccessTokenKakao(loginUser.getId(), kakaoToken, loginUser.getLoginType());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(loginUser.getNickname())
                .build();

        log.info("[getLogInResult] LogInResponse 객체에 값 주입");
        response.addHeader("Authorization", "Bearer " + loginResponse.getAccessToken());

        refreshTokenService.saveTokenInfo(loginUser.getId(), refreshToken, accessToken, 60 * 60 * 24 * 7);
        kakaoTokenService.saveKakaoTokenInfo(loginUser.getId(), kakaoToken, accessToken);

        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, token : {}", loginUser.getId(), loginResponse.getAccessToken());
        return loginResponse;
    }

    public LogoutResponse kakaoLogout(HttpServletRequest request) {
        log.info("[kakao] 로그아웃 시작");
        String accessToken;
        try {
            accessToken = jwtUtil.getTokenFromRequest(request);
        } catch (InvalidTokenException e) {
            log.error("[kakao] Access token이 유효하지 않거나 비어있습니다.", e);
            throw new InvalidTokenException("Access token이 유효하지 않거나 비어있습니다.", "Missing or invalid access token");
        }


        String kakaoToken = jwtTokenProvider.getKakaoToken(accessToken);
        if (kakaoToken == null) {
            log.error("[kakao] Kakao token 값이 null");
            throw new InvalidTokenException("Kakao token이 없습니다", "kakaoToken missing");
        }

        kakaoOauth.expireKakaoToken(kakaoToken);
        log.info("[kakao] 카카오 토큰만료 완료");
        kakaoTokenService.removeKakaoTokenInfo(accessToken);
        log.info("[kakao] 카카오 토큰제거 완료");
        memberService.logout(request);
        log.info("[kakao] 로그아웃 완료");

        return LogoutResponse.builder().success(true).build();
    }
}
