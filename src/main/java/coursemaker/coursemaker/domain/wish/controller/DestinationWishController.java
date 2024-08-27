package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.exception.WishForbiddenException;
import coursemaker.coursemaker.domain.wish.exception.WishUnauthorizedException;
import coursemaker.coursemaker.domain.wish.service.DestinationWishService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/destinationwish")
@Tag(name = "DestinationWish", description = "목적지 찜 API")
public class DestinationWishController {

    private DestinationWishService destinationWishService;

    public DestinationWishController(DestinationWishService destinationWishService) {
        this.destinationWishService = destinationWishService;
    }

    /**
     * 목적지찜 등록
     * */
    @Operation(summary = "목적지찜 등록", description = "목적지 찜 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "목적지 찜이 성공적으로 등록되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 목적지입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"존재하지 않는 목적지입니다.\"}"
                    )
            ))
    })
    @PostMapping
    public ResponseEntity<DestinationWishResponseDto> addDestinationWish(
            @RequestBody DestinationWishRequestDto requestDto,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new WishUnauthorizedException("Unauthorized", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }

        // 요청 DTO에 로그인된 사용자의 닉네임 설정
        requestDto.setNickname(logined.getNickname());

        DestinationWishResponseDto responseDto = destinationWishService.addDestinationWish(requestDto);

        // 닉네임을 숨김
        responseDto.hideMemberNickname();

        return ResponseEntity.ok(responseDto);
    }



    /**
     * 목적지찜 취소
     * */
    @Operation(summary = "목적지찜 취소", description = "등록한 목적지찜을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "목적지 찜이 성공적으로 취소되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 목적지찜을 취소할 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 목적지입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"존재하지 않는 목적지입니다.\"}"
                    )
            ))
    })
    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Void> cancelDestinationWish(
            @PathVariable Long destinationId,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new WishUnauthorizedException("Unauthorized", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }

        // 현재 로그인된 사용자의 닉네임을 가져와서 서비스에 전달
        destinationWishService.cancelDestinationWish(destinationId, logined.getNickname());
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
    }

    /**
     * 목적지찜 닉네임으로 조회
     * */
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 목적지찜 조회", description = "닉네임을 사용하여 목적지찜을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목적지 찜이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 목적지찜을 조회할 수 없습니다.")
    })
    @Parameter(name = "nickname", description = "목적지찜한 사용자의 닉네임", required = true)
    public ResponseEntity<List<DestinationWishResponseDto>> getDestinationWishesByNickname(@PathVariable String nickname,
                                                                                           @AuthenticationPrincipal LoginedInfo logined) {

        // 로그인된 사용자인지 확인
        if (logined == null) {
            throw new WishUnauthorizedException("Unauthorized", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }

        // 로그인된 사용자의 닉네임과 요청된 닉네임이 일치하는지 확인
        if (!nickname.equals(logined.getNickname())) {
            throw new WishForbiddenException("Forbidden", "다른 사용자의 목적지찜을 조회할 수 없습니다.");
        }

        List<DestinationWishResponseDto> destinationWishes = destinationWishService.getDestinationWishesByNickname(nickname);
        return ResponseEntity.ok(destinationWishes);
    }

    /**
     * 목적지찜 전체조회
     * */
    @GetMapping
    @Operation(summary = "목적지찜 목록 전체조회", description = "목적지찜 목록을 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목적지 찜이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "목적지 찜이 존재하지 않습니다.")
    })
    public ResponseEntity<List<DestinationWishResponseDto>> getAllDestinationWishes() {
        List<DestinationWishResponseDto> destinationWishes = destinationWishService.getAllDestinationWishes();
        return ResponseEntity.ok(destinationWishes);
    }
}
