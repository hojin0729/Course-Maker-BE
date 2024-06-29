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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Member", description = "현재 회원 관련 API는 다시 작성중이라서 동작하지 않습니다ㅜ")
public class MemberApiController {
    private final MemberService memberService;
    private final EmailService emailService;




    @Operation(summary = "회원 정보 수정", description = "로그인한 회원의 정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보가 정상적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            ))
    })
    @PutMapping("/me")
    public ResponseEntity<Member> updateCurrentUser(@LoginUser String nickname, @Valid @RequestBody UpdateRequest updateRequest) {
        Member updatedUser = memberService.updateUser(updateRequest, nickname);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "회원 삭제", description = "로그인한 회원을 삭제한다. (소프트 딜리트)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원이 정상적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"잘못된 요청입니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            ))
    })
    @DeleteMapping
    public ResponseEntity<Member> deleteCurrentUser(@Valid @RequestBody DeleteRequest deleteRequest) {
        Member deletedUser = memberService.deleteUser(deleteRequest);
        return ResponseEntity.ok().body(deletedUser);
    }




    @Operation(summary = "마이페이지 정보 조회", description = "마이페이지에 필요한 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 정보가 정상적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"회원 정보가 없습니다.\"}"
                    )
            ))
    })
    @GetMapping(value = "/my-page")
    public ResponseEntity<MyPageResponse> showMyPage(@LoginUser String nickname) {
        MyPageResponse myPageResponse = memberService.showMyPage(nickname);
        return ResponseEntity.ok(myPageResponse);
    }

}