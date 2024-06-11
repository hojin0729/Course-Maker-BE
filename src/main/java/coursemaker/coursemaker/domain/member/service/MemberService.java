package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.member.dto.*;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.*;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.jwt.JwtTokenProvider;
import coursemaker.coursemaker.jwt.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    public Member findById(Long userId){
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));
    }


    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));
    }

    public Member signUp(SignUpRequest signUpRequest) {
        signUpRequest.validate(); // 검증 로직 추가

        if (memberRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new UserDuplicatedException("이미 존재하는 이메일 입니다. ", "Email: " + signUpRequest.getEmail());
        }

        if (memberRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new UserDuplicatedException("이미 존재하는 닉네임 입니다. ", "닉네임: " + signUpRequest.getNickname());
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

    public Member updateUser(UpdateRequest updateRequest, String nickname) {
        updateRequest.validate(); // 검증 로직 추가
        //TODO: 수정 시 Access Token 재발급
        Member user = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));

        if (updateRequest.getNickname() != null && !updateRequest.getNickname().equals(nickname)) {
            if (memberRepository.findByNickname(updateRequest.getNickname()).isPresent()) {
                throw new UserDuplicatedException("이미 존재하는 닉네임입니다.", "Nickname: " + updateRequest.getNickname());
            }
            user.setNickname(updateRequest.getNickname());
        }

        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        if (updateRequest.getProfileImgUrl() != null) {
            user.setProfileImgUrl(updateRequest.getProfileImgUrl());
        }
        if (updateRequest.getProfileDescription() != null) {
            user.setProfileDescription(updateRequest.getProfileDescription());
        }

        return memberRepository.save(user);
    }

    public Member deleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getUserId() == null) {
            throw new IllegalUserArgumentException("유효하지 않은 요청입니다. 유저 ID가 필요합니다.", "deleteRequest or userId is null");
        }

        Long userId = deleteRequest.getUserId();

        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));

        user.setDeletedAt(LocalDateTime.now());

        try {
            Member deletedUser = memberRepository.save(user);
            return deletedUser;
        } catch (Exception e) {
            log.error("Error occurred while deleting user with ID: {}", userId, e);
            throw new RuntimeException("회원 삭제 중 오류가 발생했습니다.");
        }
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

        /*db에 리프레시토큰이랑 엑세스토큰 저장*/
        refreshTokenService.saveTokenInfo(loginUser.getId(), refreshToken, accessToken, 60 * 60 * 24 * 7);

        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, loginResponse.getAccessToken());
        return loginResponse;

    }

    public LogoutResponse logout(HttpServletRequest request) {
//        // 쿠키 만료 시작
//        Cookie cookieForExpire = new Cookie("Authorization", null);
//        cookieForExpire.setPath("/");
//        cookieForExpire.setMaxAge(0);
//        response.addCookie(cookieForExpire); // 생성 즉시 만료되는 쿠키로 덮어씌움
        //쿠키 만료 끝

//        //리프레시 토큰 삭제 시작
//        Cookie currentCookie = Arrays.stream(request.getCookies())
//                .filter(cookie -> "Authorization".equals(cookie.getName()))
//                .findFirst().orElseThrow();
//        String token = URLDecoder.decode(currentCookie.getValue(), StandardCharsets.UTF_8);

        String token = request.getHeader("Authorization");

        //TODO:예외처리
        if (token == null) {
            throw new UnauthorizedException("인증받지 않은 회원입니다. ", "");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
//        refreshTokenService.setBlackList(token);//TODO 토큰 블랙리스트 등록
        //리프레시 토큰 삭제 끝
        refreshTokenService.removeTokenInfo(token);

        LogoutResponse logoutResponse = LogoutResponse.builder().success(true).build();
        log.info("[logIn] 정상적으로 로그아웃되었습니다.");
        return logoutResponse;
    }


    public MyPageResponse showMyPage(String nickname) {
        Member currentUser = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));

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

    public boolean checkExistByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

}