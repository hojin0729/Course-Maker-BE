package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.service.DestinationWishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /*목적지찜 등록*/
    @PostMapping
    @Operation(summary = "목적지찜 등록", description = "목적지 찜을 등록합니다.")
    public ResponseEntity<DestinationWishResponseDto> addDestinationWish(
            @RequestBody DestinationWishRequestDto requestDto,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 인증된 사용자와 요청된 nickname이 일치하는지 확인
        if (!logined.getNickname().equals(requestDto.getNickname())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        DestinationWishResponseDto responseDto = destinationWishService.addDestinationWish(requestDto);
        return ResponseEntity.ok(responseDto);
    }



    /*목적지찜 취소*/
    @Operation(summary = "목적지찜 취소", description = "등록한 목적지찜을 취소합니다.")
    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Void> cancelDestinationWish(
            @PathVariable Long destinationId,
            @AuthenticationPrincipal LoginedInfo logined) {

        // 인증된 사용자와 요청된 memberId가 일치하는지 확인
        String nickname = logined.getNickname();
        destinationWishService.cancelDestinationWish(destinationId, nickname);
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
    }

    /*목적지찜 닉네임으로 조회*/
    @GetMapping("/{nickname}")
    @Operation(summary = "닉네임으로 목적지찜 조회", description = "닉네임을 사용하여 목적지찜을 조회합니다.")
    @Parameter(name = "nickname", description = "목적지찜한 사용자의 닉네임", required = true)
    public ResponseEntity<List<DestinationWishResponseDto>> getDestinationWishesByNickname(@PathVariable String nickname) {
        List<DestinationWishResponseDto> destinationWishes = destinationWishService.getDestinationWishesByNickname(nickname);
        return ResponseEntity.ok(destinationWishes);
    }

    /*목적지찜 전체조회*/
    @GetMapping
    @Operation(summary = "목적지찜 목록 전체조회", description = "목적지찜 목록을 전체 조회합니다.")
    public ResponseEntity<List<DestinationWishResponseDto>> getAllDestinationWishes() {
        List<DestinationWishResponseDto> destinationWishes = destinationWishService.getAllDestinationWishes();
        return ResponseEntity.ok(destinationWishes);
    }
}
