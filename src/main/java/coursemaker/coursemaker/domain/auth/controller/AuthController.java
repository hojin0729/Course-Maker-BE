package coursemaker.coursemaker.domain.auth.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.auth.dto.join_withdraw.JoinRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.join_withdraw.JoinResponseDTO;
import coursemaker.coursemaker.domain.auth.dto.join_withdraw.WithdrawalRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.jwt.ReIssueRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.jwt.ReIssueResponseDTO;
import coursemaker.coursemaker.domain.auth.dto.login_logout.LoginRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.login_logout.LoginResponseDTO;
import coursemaker.coursemaker.domain.auth.dto.login_logout.LogoutRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.role.RoleUpdateDTO;
import coursemaker.coursemaker.domain.auth.dto.validate.NicknameValidateRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.validate.SendValidateCodeRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.validate.ValidateEmailRequestDTO;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.auth.service.NicknameValidate;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
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
    public ResponseEntity<JoinResponseDTO> signUp(@Valid @RequestBody JoinRequestDTO joinRequest) {
        JoinResponseDTO joinResponse = authService.join(joinRequest);
        return ResponseEntity.ok().body(joinResponse);
    }

    /*스웨거용 더미 로그인 컨트롤러*/
    @Operation(summary = "회원 로그인", description = "이메일과 비밀번호로 로그인 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인되었습니다."),
            @ApiResponse(responseCode = "400", description = "비밀번호가 잘못되었습니다.", content = @Content(
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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok().body(null);
    }


    @Operation(summary = "access token 재발행", description = "refresh token을 이용해 access token을 재발행 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 토큰이 재발행 됨."),
            @ApiResponse(responseCode = "401", description = "1. 토큰이 유효하지 않음.(Invalid token)\t\n2. 리프레시 토큰의 유효기간이 만료됨.(Expired token)", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"Invalid token\", \"message\": \"인증되지 않은 토큰입니다.\"}"
                    )
            ))
    })
    @PostMapping("/reissue")
    public ResponseEntity<ReIssueResponseDTO> reissue(@RequestBody ReIssueRequestDTO reissueRequest) {

        ReIssueResponseDTO dto = authService.reissueToken(reissueRequest);

        return ResponseEntity.ok().body(dto);
    }


    @Operation(summary = "회원 로그아웃", description = "현재 유저를 로그아웃 합니다. 프론트에서도 리프레시 토큰 및 엑세스 토큰을 삭제해 주세여.")
    @ApiResponse(
            responseCode = "200", description = "정상적으로 로그아웃되었습니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDto) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 회원을 탈퇴합니다. 프론트에서도 회원 탈퇴 후 로그아웃을 위해 리프레시 토큰 및 엑세스 토큰을 삭제해 주세여.")
    @ApiResponse(
            responseCode = "200", description = "정상적으로 로그아웃되었습니다."
    )
    @DeleteMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal(@AuthenticationPrincipal LoginedInfo loginedInfo,
                                           @RequestBody WithdrawalRequestDTO withdrawalRequestDto) {
        jwtProvider.expireRefreshToken(withdrawalRequestDto.getRefreshToken());
        authService.withdrawal(loginedInfo.getNickname());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임 검증", description = "닉네임이 존재 하는지, 닉네임이 정책에 맞는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임일 경우"),
            @ApiResponse(responseCode = "400", description = "닉네임 패턴이 일치하지 않음.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"닉네임은 2~10글자의 한글과 영어 소문자로 구성되야 합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임일 경우", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"이미 사용중인 닉네임 입니다.\"}"
                    )
            ))
    })
    @PostMapping("/validate/nickname")
    public ResponseEntity<Void> validateNickname(@Valid @RequestBody NicknameValidateRequestDTO nickname){
        NicknameValidate validate = authService.validateNickname(nickname.getNickname());

        if(validate == NicknameValidate.IS_EXIST){// 닉네임 중복
            throw new UserDuplicatedException("이미 사용중인 닉네임 입니다.", "[AUTH] 사용자 중복 검증: "+ nickname.getNickname());// 409 예외
        } else if(validate == NicknameValidate.IS_NOT_MATCH_PATTERN){// 패턴이 안맞음
            throw new IllegalArgumentException("닉네임은 2~10글자의 한글과 영어 소문자로 구성되야 합니다.");// 400 예외
        } else{
            return ResponseEntity.ok().build();
        }

    }

    @Operation(summary = "이메일 코드 전송", description = "해당 이메일로 인증 코드를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 전송"),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 올바르지 않은 경우", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"이메일은 example@email.com 과 같은 이메일 형식이어야 합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일인 경우", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"이미 존재하는 이메일 입니다.\"}"
                    )
            ))
    })
    @PostMapping("/validate/email")
    public ResponseEntity<Void> validateEmail(@Valid @RequestBody SendValidateCodeRequestDTO sendValidateCodeRequestDTO) {
        authService.sendValidationCode(sendValidateCodeRequestDTO);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 코드 검증", description = "이메일로 전송된 코드와 입력한 코드가 일치하는지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 검증 성공"),
            @ApiResponse(responseCode = "400", description = "1. 이메일 형식이 올바르지 않은 경우(Illegal argument)\t\n 1. 인증 시간이 만료된 경우(Timeout)\t\n2. 전송된 검증 코드와 일치하지 않은 경우(Mismatch code)\"", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"이메일은 example@email.com 과 같은 이메일 형식이어야 합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "이메일이 전송되지 않은 경우", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Duplicated item\", \"message\": \"이미 존재하는 이메일 입니다.\"}"
                    )
            ))
    })
    @PostMapping("/validate/email/code")
    public ResponseEntity<Void> validateEmailCode(@Valid @RequestBody ValidateEmailRequestDTO dto) {

        authService.validateEmail(dto);
        return ResponseEntity.ok().build();
    }

    /*백엔드 롤 강제 업데이트 API*/
    @Operation(summary = "등급 강제 업데이트", description = "백엔드에서 테스트용으로 만든 API 입니다.")
    @PostMapping("/role-update")
    public ResponseEntity<Void> updateRole(@RequestBody RoleUpdateDTO dto){
        authService.updateRole(dto);

        return ResponseEntity.ok().build();
    }
}
