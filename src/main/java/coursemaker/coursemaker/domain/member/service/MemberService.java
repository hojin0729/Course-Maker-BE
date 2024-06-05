package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.member.dto.*;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.IllegalUserArgumentException;
import coursemaker.coursemaker.domain.member.exception.InvalidPasswordException;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Member findById(Long userId){
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));
    }


    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));
    }

    public Member signUp(SignUpRequest signUpRequest) {
        if (memberRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new UserDuplicatedException("이미 존재하는 이메일 입니다. ", "Email: " + signUpRequest.getEmail());
        }
        if(signUpRequest.getEmail() == null) {
            throw new IllegalUserArgumentException("올바른 값을 ", "");
        }
        String email = signUpRequest.getEmail();
        Member.LoginType loginType = Member.LoginType.BASIC; //일반 이메일 로그인
        String name = signUpRequest.getName();
        String nickname = signUpRequest.getNickname();
        String rawPassword = signUpRequest.getPassword();
        String phoneNumber = signUpRequest.getPhoneNumber();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String profileImg = signUpRequest.getProfileImgUrl();
        String profileDescription = signUpRequest.getProfileDescription();
        String roles = "ROLE_USER"; // 기본값

        Member builtUser = Member.addMemberBuilder()
                .email(email)
                .loginType(loginType)
                .name(name)
                .nickname(nickname)
                .password(encodedPassword)
                .phoneNumber(phoneNumber)
                .profileImgUrl(profileImg)
                .profileDescription(profileDescription)
                .roles(roles)
                .build();

        Member newUser = memberRepository.save(builtUser);
        return newUser;
    }

    public Member updateUser(UpdateRequest updateRequest) {
        //TODO: 회원 정보 수정 시 Access Token 재발급 해야함
        //TODO: Optional 예외처리
        Member user = memberRepository
                .findById(updateRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + updateRequest.getUserId()));

        String name = updateRequest.getName();
        String nickname = updateRequest.getNickname();
        String password = updateRequest.getPassword();
        String profileImg = updateRequest.getProfileImgUrl();
        String profileDescription = updateRequest.getProfileDescription();

        if(name != null) {
            user.setName(name);
        }
        if(nickname != null) {
            user.setNickname(nickname);
        }
        if(password != null) {
            user.setPassword(password);
        }
        if(profileImg != null) {
            user.setProfileImgUrl(profileImg);
        }
        if(profileDescription != null) {
            user.setProfileDescription(profileDescription);
        }

        Member updatedUser = memberRepository.save(user);
        return updatedUser;
    }

    public Member deleteUser(DeleteRequest deleteRequest) {

        Long userId = deleteRequest.getUserId();

        //TODO: 예외처리
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));

        user.setDeletedAt(LocalDateTime.now());

        Member deletedUser = memberRepository.save(user);
        return deletedUser;
    }

    public LoginResponse login(String id, String rawPassword, HttpServletResponse response) {
        LoginResponse loginResponse = new LoginResponse();

        Member loginUser = memberRepository.findByEmail(id)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Email: " + id));
        String encodedPassword = loginUser.getPassword();
        log.info("[getSignInResult] Id : {}", id);

        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            log.error("[getSignInResult] 패스워드 불일치");
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.", "Password: " + rawPassword);
        }
        log.info("[getLogInResult] 패스워드 일치");
        log.info("[getLogInResult] LogInResponse 객체 생성");
        String accessToken = jwtTokenProvider.createAccessToken(
                loginUser.getNickname()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken();

        loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();


        log.info("[getLogInResult] LogInResponse 객체에 값 주입");
        var cookie1 = new Cookie("Authorization", URLEncoder.encode("Bearer " + loginResponse.getAccessToken(), StandardCharsets.UTF_8));
        cookie1.setPath("/");
        cookie1.setMaxAge(60 * 60);
        response.addCookie(cookie1);

//        refreshTokenService.saveTokenInfo(loginUser.getId(), refreshToken, accessToken, 60 * 60 * 24 * 7);

        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, loginResponse.getAccessToken());
        return loginResponse;

    }

//    public LogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {
//        // 쿠키 만료 시작
//        Cookie cookieForExpire = new Cookie("Authorization", null);
//        cookieForExpire.setPath("/");
//        cookieForExpire.setMaxAge(0);
//        response.addCookie(cookieForExpire); // 생성 즉시 만료되는 쿠키로 덮어씌움
//        //쿠키 만료 끝
//
//        //리프레시 토큰 삭제 시작
//        Cookie currentCookie = Arrays.stream(request.getCookies())
//                .filter(cookie -> "Authorization".equals(cookie.getName()))
//                .findFirst().orElseThrow();
//        String token = URLDecoder.decode(currentCookie.getValue(), StandardCharsets.UTF_8);
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        refreshTokenService.removeTokenInfo(token);
//        //리프레시 토큰 삭제 끝
//
//        LogoutResponse logoutResponse = LogoutResponse.builder().success(true).build();
//        return logoutResponse;
//    }


    public MyPageResponse showMyPage(Long userId) {
        Member currentUser = memberRepository.findById(userId)
                .orElseThrow();
        String nickname = currentUser.getNickname();
        String name = currentUser.getName();
        String profileDescription = currentUser.getProfileDescription();
        String profileImgUrl = currentUser.getProfileImgUrl();

        MyPageResponse myPageResponse = MyPageResponse.builder()
                .nickname(nickname)
                .name(name)
                .profileDescription(profileDescription)
                .profileImgUrl(profileImgUrl)
                .build();

        return myPageResponse;
    }

    public ValidateNicknameResponse isValid(ValidateNicknameRequest validateNicknameRequest) {
        String nickname = validateNicknameRequest.getNickname();

        // 중복 여부 확인
        Boolean isDuplicate = memberRepository.findByNickname(nickname).isPresent();

        // 조건 불일치 여부 확인
        String regex = "[a-zA-Z가-힣]{2,10}";
        Boolean isInappropriate = !nickname.matches(regex);

        // 닉네임 유효 여부 반환
        ValidateNicknameResponse validateNicknameResponse = ValidateNicknameResponse.builder()
                .isDuplicate(isDuplicate)
                .isInappropriate(isInappropriate)
                .build();

        return validateNicknameResponse;
    }

    //TODO: 작성중
    public ValidateEmailResponse isEmailValid(ValidateEmailRequest validateEmailRequest) {
        String email = validateEmailRequest.getEmail();

        // 이메일 중복 여부 확인
        Boolean isDuplicate = memberRepository.findByEmail(email).isPresent();

        // 조건 불일치 여부 확인
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Boolean isInappropriate = !email.matches(emailRegex);

        // 이메일 유효 여부 반환
        ValidateEmailResponse validateEmailResponse = ValidateEmailResponse.builder()
                .isDuplicate(isDuplicate)
                .isInappropriate(isInappropriate)
                .build();

        return validateEmailResponse;
    }

}