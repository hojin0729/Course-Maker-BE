package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.member.dto.AccountCreateDto;
import coursemaker.coursemaker.domain.member.dto.AccountDto;
import coursemaker.coursemaker.domain.member.dto.AccountUpdateDto;
import coursemaker.coursemaker.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/member")
public class RestApiController {
    private final MemberService memberService;

    public RestApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "사용자 정보 조회", description = "인증된 사용자의 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 사용자 정보를 조회하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    @GetMapping(value="/user")
    public AccountDto restUser(@AuthenticationPrincipal AccountDto accountDto) {
        return accountDto;
    }

    @Operation(summary = "여행자 정보 조회", description = "인증된 여행자의 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 여행자 정보를 조회하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    @GetMapping(value="/traveler")
    public AccountDto restTraveler(@AuthenticationPrincipal AccountDto accountDto) {
        return accountDto;
    }

    @Operation(summary = "관리자 정보 조회", description = "인증된 관리자의 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 관리자 정보를 조회하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    @GetMapping(value="/admin")
    public AccountDto restAdmin(@AuthenticationPrincipal AccountDto accountDto) {
        return accountDto;
    }

    @Operation(summary = "로그아웃", description = "인증된 사용자를 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 로그아웃하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "logout";
    }

    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<AccountDto> createMember(@RequestBody AccountCreateDto request) {
        AccountDto accountDto = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    @Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllMembers() {
        List<AccountDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "회원 조회", description = "ID를 사용하여 특정 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getMemberById(@PathVariable Long id) {
        AccountDto accountDto = memberService.getMemberById(id);
        return ResponseEntity.ok(accountDto);
    }

    @Operation(summary = "회원 수정", description = "ID를 사용하여 특정 회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateMember(@PathVariable Long id, @RequestBody AccountUpdateDto request) {
        AccountDto updatedMember = memberService.updateMember(id, request);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "회원 삭제", description = "ID를 사용하여 특정 회원 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}