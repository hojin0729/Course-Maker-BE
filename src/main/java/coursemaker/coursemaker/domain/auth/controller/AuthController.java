package coursemaker.coursemaker.domain.auth.controller;

import coursemaker.coursemaker.domain.auth.dto.*;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Auth", description = "로그인, 로그아웃, 회원가입등의 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
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
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> signUp(@Valid @RequestBody JoinRequestDto joinRequest) {
        JoinResponseDto joinResponse = authService.join(joinRequest);
        return ResponseEntity.ok().body(joinResponse);
    }

    /*스웨거용 더미 로그인 컨트롤러*/
    @Operation(summary = "회원 로그인", description = "이메일과 비밀번호로 로그인 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인되었습니다."),
            @ApiResponse(responseCode = "401", description = "비밀번호가 잘못되었습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"Authentication failed\", \"message\": \"비밀번호가 일치하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "인증 실패: 사용자를 찾을 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"존재하지 않는 회원입니다.\"}"
                    )
            ))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok().body(null);
    }


    @Operation(summary = "access token 재발행", description = "refresh token을 이용해 access token을 재발행 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 토큰이 재발행 됨."),
            @ApiResponse(responseCode = "401", description = "토큰이 유효하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"Invalid token\", \"message\": \"인증되지 않은 토큰입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "토큰의 유효기간이 만료됨.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"Expired token\", \"message\": \"토큰이 만료됬습니다.\"}"
                    )
            ))
    })
    @PostMapping("/reissue")
    public ResponseEntity<ReIssueResponseDto> reissue(@RequestBody ReIssueRequestDto     reissueRequest) {

        ReIssueResponseDto dto = authService.reissueToken(reissueRequest);

        return ResponseEntity.ok().body(dto);
    }


    @Operation(summary = "회원 로그아웃", description = "현재 유저를 로그아웃 합니다. 프론트에서도 리프레시 토큰 및 엑세스 토큰을 삭제해 주세여.")
    @ApiResponse(
            responseCode = "200", description = "정상적으로 로그아웃되었습니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return ResponseEntity.ok().build();
    }


    @PostMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal(@AuthenticationPrincipal Member member,
                                           @RequestBody WithdrawalRequestDto withdrawalRequestDto) {

        System.out.println("member = " + member);
        jwtProvider.expireRefreshToken(withdrawalRequestDto.getRefreshToken());
        authService.withdrawal(member.getNickname());
        return ResponseEntity.ok().build();
    }
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
