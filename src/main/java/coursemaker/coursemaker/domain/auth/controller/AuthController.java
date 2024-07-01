package coursemaker.coursemaker.domain.auth.controller;

import coursemaker.coursemaker.domain.auth.dto.JoinRequestDto;
import coursemaker.coursemaker.domain.auth.dto.JoinResponseDto;
import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.member.dto.*;
import coursemaker.coursemaker.domain.member.service.EmailService;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Auth", description = "로그인, 로그아웃, 회원가입등의 인증 관련 API")
public class AuthController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final AuthService authService;

    @Operation(summary = "회원 생성", description = "회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원이 정상적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "생성하려는 회원의 인자값이 올바르지 않을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"유효하지 않은 이메일 형식입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원일 경우 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"이미 존재하는 닉네임입니다.\"}"
                    )
            ))
    })
    @PostMapping(value = "/join")
    public ResponseEntity<JoinResponseDto> signUp(@Valid @RequestBody JoinRequestDto joinRequest) {
        JoinResponseDto joinResponse = authService.join(joinRequest);
        return ResponseEntity.ok().body(joinResponse);
    }

//    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호로 로그인한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "정상적으로 로그인되었습니다."),
//            @ApiResponse(responseCode = "401", description = "인증 실패: 비밀번호가 잘못되었습니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 401, \"errorType\": \"Authentication failed\", \"message\": \"비밀번호가 잘못되었습니다.\"}"
//                    )
//            )),
//            @ApiResponse(responseCode = "404", description = "인증 실패: 사용자를 찾을 수 없습니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"회원 정보가 없습니다.\"}"
//                    )
//            ))
//    })
//    @PostMapping(value = "/login")
//    public ResponseEntity<LoginResponse> loginBasic(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        String id = loginRequest.getLoginEmail();
//        String password = loginRequest.getPassword();
//        LoginResponse loginResponse = memberService.login(id, password, response);
//        log.info("[logIn] 정상적으로 로그인되었습니다. id : {}, nickname : {}, token : {}", id, loginResponse.getNickname(), loginResponse.getAccessToken());
//
//        //TODO: 유저 정보 Cookie에 저장
//        return ResponseEntity.ok().body(loginResponse);
//    }
//
//    @Operation(summary = "회원 로그아웃", description = "현재 유저를 로그아웃한다: 쿠키 만료, 리프레시 토큰 삭제.")
//    @ApiResponse(
//            responseCode = "200", description = "정상적으로 로그아웃되었습니다."
//    )
//    @PostMapping("/logout")
//    public ResponseEntity<LogoutResponse> logoutBasic(HttpServletRequest request, HttpServletResponse response) {
//        LogoutResponse logoutResponse = memberService.logout(request);
//        return ResponseEntity.ok().body(logoutResponse);
//    }
//
//
//    @Operation(summary = "닉네임 유효 확인", description = "회원가입 및 회원정보 수정 시, 중복 또는 글자 수 등, 닉네임 유효 여부를 검증한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "닉네임 유효성이 정상적으로 확인되었습니다."),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
//                    )
//            )),
//            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"이미 존재하는 닉네임입니다.\"}"
//                    )
//            ))
//    })
//    @PostMapping(value = "/validate-nickname")
//    public ResponseEntity<ValidateNicknameResponse> validateNickname(@Valid @RequestBody ValidateNicknameRequest validateNicknameRequest) {
//        ValidateNicknameResponse validateNicknameResponse = memberService.isValid(validateNicknameRequest);
//        return ResponseEntity.ok(validateNicknameResponse);
//    }
//
//    @Operation(summary = "이메일 유효 확인", description = "회원가입 및 회원정보 수정 시,  중복 또는 글자 수 등, 이메일 중복 여부를 검증한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "이메일 유효성이 정상적으로 확인되었습니다."),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
//                    )
//            )),
//            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"이미 존재하는 이메일입니다.\"}"
//                    )
//            ))
//    })
//    @PostMapping(value = "/validate-email")
//    public ResponseEntity<ValidateEmailResponse> validateEmail(@Valid @RequestBody ValidateEmailRequest validateEmailRequest) {
//        ValidateEmailResponse validateEmailResponse = memberService.isEmailValid(validateEmailRequest);
//        return ResponseEntity.ok(validateEmailResponse);
//    }
//
//    @Operation(summary = "이메일 검증", description = "이메일 검증을 위한 인증코드를 해당 메일로 발송한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "이메일 인증 코드가 정상적으로 발송되었습니다."),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
//                    )
//            ))
//    })
//    @PostMapping("/signup/send-validate")
//    public ResponseEntity<ValidateEmailResponse> SendMailToValidate(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException {
//        ValidateEmailResponse validateEmailResponse = emailService.sendValidateSignupMail(emailRequest.getEmail());
//        return ResponseEntity.ok(validateEmailResponse);
//    }
//
//    @Operation(summary = "인증코드 검증", description = "이메일 인증코드의 유효성을 검증한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "이메일 인증 코드가 일치합니다."),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ErrorResponse.class),
//                    examples = @ExampleObject(
//                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
//                    )
//            ))
//    })
//    @PostMapping("/signup/verify-validate")
//    public ResponseEntity<EmailCodeVerifyResponse> verifyEmailCode(@Valid @RequestBody EmailCodeVerifyRequest emailCodeVerifyRequest) {
//        EmailCodeVerifyResponse emailCodeVerifyResponse = emailService.verifyEmailCode(emailCodeVerifyRequest);
//        return ResponseEntity.ok(emailCodeVerifyResponse);
//    }
}
