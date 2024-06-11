package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.member.dto.*;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.IllegalUserArgumentException;
import coursemaker.coursemaker.domain.member.exception.InvalidPasswordException;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.service.EmailService;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.util.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Member", description = "회원 API")
public class MemberApiController {
    private final MemberService memberService;
    private final EmailService emailService;


    @Operation(summary = "회원 생성", description = "기본 회원가입 후 유저를 생성한다.")
    @ApiResponse(
            responseCode = "200", description = "회원이 정상적으로 생성되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @ApiResponse(
            responseCode = "409", description = "이미 존재하는 회원입니다.", content = @Content
    )
    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        // TODO: 이메일 인증 등의 절차가 모두 완료되었는지 확인 후 회원가입이 진행되어야 함
        memberService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 수정", description = "로그인한 회원의 정보를 수정한다.")
    @ApiResponse(
            responseCode = "200", description = "회원 정보가 정상적으로 수정되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @PutMapping("/me")
    public ResponseEntity<Member> updateCurrentUser(@LoginUser String nickname, @Valid @RequestBody UpdateRequest updateRequest) {
        Member updatedUser = memberService.updateUser(updateRequest, nickname);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "회원 삭제", description = "로그인한 회원을 삭제한다. (소프트 딜리트)")
    @ApiResponse(
            responseCode = "200", description = "회원이 정상적으로 삭제되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @DeleteMapping
    public ResponseEntity<Member> deleteCurrentUser(@Valid @RequestBody DeleteRequest deleteRequest) {
        Member deletedUser = memberService.deleteUser(deleteRequest);
        return ResponseEntity.ok().body(deletedUser);
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호로 로그인한다.")
    @ApiResponse(
            responseCode = "200", description = "정상적으로 로그인되었습니다."
    )
    @ApiResponse(
            responseCode = "401", description = "인증 실패: 비밀번호가 잘못되었습니다.", content = @Content
    )
    @ApiResponse(
            responseCode = "404", description = "인증 실패: 사용자를 찾을 수 없습니다.", content = @Content
    )
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> loginBasic(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String id = loginRequest.getLoginEmail();
        String password = loginRequest.getPassword();
        LoginResponse loginResponse = memberService.login(id, password, response);
        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, nickname : {}, token : {}", id, loginResponse.getNickname(), loginResponse.getAccessToken());

        //TODO: 유저 정보 Cookie에 저장
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "회원 로그아웃", description = "현재 유저를 로그아웃한다: 쿠키 만료, 리프레시 토큰 삭제.")
    @ApiResponse(
            responseCode = "200", description = "정상적으로 로그아웃되었습니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logoutBasic(HttpServletRequest request, HttpServletResponse response) {
        LogoutResponse logoutResponse = memberService.logout(request);
        return ResponseEntity.ok().body(logoutResponse);
    }


    @Operation(summary = "마이페이지 정보 조회", description = "마이페이지에 필요한 정보를 조회한다.")
    @ApiResponse(
            responseCode = "200", description = "마이페이지 정보가 정상적으로 조회되었습니다."
    )
    @ApiResponse(
            responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content
    )
    @GetMapping(value = "/my-page")
    public ResponseEntity<MyPageResponse> showMyPage(@LoginUser String nickname) {
        MyPageResponse myPageResponse = memberService.showMyPage(nickname);
        return ResponseEntity.ok(myPageResponse);
    }

    @Operation(summary = "닉네임 유효 확인", description = "회원가입 및 회원정보 수정 시, 중복 또는 글자 수 등, 닉네임 유효 여부를 검증한다.")
    @ApiResponse(
            responseCode = "200", description = "닉네임 유효성이 정상적으로 확인되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @ApiResponse(
            responseCode = "409", description = "이미 존재하는 닉네임입니다.", content = @Content
    )
    @PostMapping(value = "/validate-nickname")
    public ResponseEntity<ValidateNicknameResponse> validateNickname(@Valid @RequestBody ValidateNicknameRequest validateNicknameRequest) {
        ValidateNicknameResponse validateNicknameResponse = memberService.isValid(validateNicknameRequest);
        return ResponseEntity.ok(validateNicknameResponse);
    }

    @Operation(summary = "이메일 유효 확인", description = "회원가입 및 회원정보 수정 시,  중복 또는 글자 수 등, 이메일 중복 여부를 검증한다.")
    @ApiResponse(
            responseCode = "200", description = "이메일 유효성이 정상적으로 확인되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @ApiResponse(
            responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content
    )
    @PostMapping(value = "/validate-email")
    public ResponseEntity<ValidateEmailResponse> validateEmail(@Valid @RequestBody ValidateEmailRequest validateEmailRequest) {
        ValidateEmailResponse validateEmailResponse = memberService.isEmailValid(validateEmailRequest);
        return ResponseEntity.ok(validateEmailResponse);
    }

    @Operation(summary = "이메일 검증", description = "이메일 검증을 위한 인증코드를 해당 메일로 발송한다.")
    @ApiResponse(
            responseCode = "200", description = "이메일 인증 코드가 정상적으로 발송되었습니다."
    )
    @ApiResponse(
            responseCode = "400", description = "잘못된 요청입니다.", content = @Content
    )
    @PostMapping("/send-validate")
    public ResponseEntity<ValidateEmailResponse> SendMailToValidate(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException {
        ValidateEmailResponse validateEmailResponse = emailService.sendValidateSignupMail(emailRequest.getEmail());
        return ResponseEntity.ok(validateEmailResponse);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(IllegalUserArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalUserArgumentException(IllegalUserArgumentException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()

                .value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}