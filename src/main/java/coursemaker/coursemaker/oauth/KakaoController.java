package coursemaker.coursemaker.oauth;

import coursemaker.coursemaker.domain.member.dto.LoginResponse;
import coursemaker.coursemaker.domain.member.dto.LogoutResponse;
import coursemaker.coursemaker.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Kakao", description = "카카오 소셜 로그인")
public class KakaoController {
    private final KakaoOauth kakaoOauth;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String showLogInForm(Model model) {
        model.addAttribute("kakaoApiKey", kakaoOauth.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoOauth.getKakaoLoginRedirectUri());
        model.addAttribute("redirectLogoutUri", kakaoOauth.getKakaoLogoutRedirectUri());
        return "login/login";
    }


    @Operation(summary = "로그인", description = "카카오 로그인")
    @RequestMapping(value = "/login/oauth2/code/kakao", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        String kakaoAccessToken = kakaoOauth.getKakaoAccessToken(code);
        Map<String, Object> userInfo = kakaoOauth.getUserInfoFromKakaoToken(kakaoAccessToken);

        String id = (String) userInfo.get("id");
        String nickname = (String) userInfo.get("nickname");

        if(!memberService.checkExistByEmail(id + "@coursemaker.com")){
            kakaoService.kakaoSignUp(id, nickname);
        }

        LoginResponse loginResponse = kakaoService.kakaoLogin(id, response, kakaoAccessToken);

        log.info("[kakaoLogin] 카카오 닉네임: {}", nickname);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "로그아웃", description = "카카오 로그아웃")
    @RequestMapping(value = "/auth/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LogoutResponse> kakaoLogout(HttpServletRequest request) {
        LogoutResponse logoutResponse = kakaoService.kakaoLogout(request);
        return ResponseEntity.ok().body(logoutResponse);
    }
}

