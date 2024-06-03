package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.member.dto.*;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.EmailService;
import coursemaker.coursemaker.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member API")
@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final MemberService memberService;
    private final EmailService emailService;


    @Operation(summary = "Create User API", description = "기본 회원가입 후 유저를 생성한다.")
    @PostMapping
    public ResponseEntity<Member> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        // TODO: 이메일 인증 등의 절차가 모두 완료되었는지 확인 후 회원가입이 진행되어야 함
        Member newUser = memberService.signUp(signUpRequest);
        return ResponseEntity.ok().body(newUser);
    }

    @Operation(summary = "Update User API", description = "로그인한 회원의 정보를 수정한다.")
    @PutMapping
    public ResponseEntity<Member> updateCurrentUser(@Valid @RequestBody UpdateRequest updateRequest) {
        Member updatedUser = memberService.updateUser(updateRequest);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Operation(summary = "Delete User API", description = "로그인한 회원을 삭제한다. (소프트 딜리트)")
    @DeleteMapping
    public ResponseEntity<Member> deleteCurrentUser(@Valid @RequestBody DeleteRequest deleteRequest) {
        Member deletedUser = memberService.deleteUser(deleteRequest);
        return ResponseEntity.ok().body(deletedUser);
    }

    @Operation(summary = "User Login API", description = "아이디와 비밀번호로 로그인한다.")
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> loginBasic(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String id = loginRequest.getLoginEmail();
        String password = loginRequest.getPassword();
        LoginResponse loginResponse = memberService.login(id, password, response);
        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, loginResponse.getAccessToken());

        //TODO: 유저 정보 Cookie에 저장
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "User MyPage API", description = "마이페이지에 필요한 정보를 조회한다.")
    @GetMapping(value = "/my-page")
    public ResponseEntity<MyPageResponse> showMyPage(@Valid @RequestParam("userId") Long userId) {
        MyPageResponse myPageResponse = memberService.showMyPage(userId);
        return ResponseEntity.ok(myPageResponse);
    }

    @Operation(summary = "Validate Nickname", description = "회원가입 및 회원정보 수정 시, 중복 또는 글자 수 등, 닉네임 유효 여부를 검증한다.")
    @PostMapping(value = "/validate-nickname")
    public ResponseEntity<ValidateNicknameResponse> validateNickname(@Valid @RequestBody ValidateNicknameRequest validateNicknameRequest) {
        ValidateNicknameResponse validateNicknameResponse = memberService.isValid(validateNicknameRequest);
        return ResponseEntity.ok(validateNicknameResponse);
    }

    @Operation(summary = "Send Mail To Validate Email", description = "이메일 검증을 위한 인증코드를 해당 메일로 발송한다.")
    @PostMapping("/send-validate")
    public ResponseEntity<ValidateEmailResponse> SendMailToValidate(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException {
        ValidateEmailResponse validateEmailResponse = emailService.sendValidateSignupMail(emailRequest.getEmail());
        return ResponseEntity.ok(validateEmailResponse);
    }
}