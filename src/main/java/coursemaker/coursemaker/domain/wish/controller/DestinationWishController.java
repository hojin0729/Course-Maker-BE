package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.service.DestinationWishService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/destinationwish")
@io.swagger.v3.oas.annotations.tags.Tag(name = "DestinationWish", description = "목적지 찜 API")
public class DestinationWishController {

    private DestinationWishService destinationWishService;
    private DestinationWish destinationWish;


    /*목적지찜 등록*/
    @PostMapping
    public ResponseEntity<DestinationWish> addDestinationWish(@RequestParam Long destinationId, @RequestParam Long memberId) {
        DestinationWish destinationWish = destinationWishService.addDestinationWish(destinationId, memberId);
        return ResponseEntity.ok(destinationWish);
    }



    /*목적지찜 취소*/
    @DeleteMapping
    public ResponseEntity<Void> cancelDestinationWish(@RequestParam Long destinationId, @RequestParam Long memberId) {
        destinationWishService.cancelDestinationWish(destinationId, memberId);
        return ResponseEntity.noContent().build();
    }

    /*목적지찜 닉네임으로 조회*/
    @Operation(summary = "닉네임으로 목적지찜 목록 조회")
    @GetMapping("/{nickname}")
    public ResponseEntity<List<DestinationWish>> getDestinationWishesByNickname(@RequestParam String nickname) {
        List<DestinationWish> destinationWishes = destinationWishService.getDestinationWishesByNickname(nickname);
        return ResponseEntity.ok(destinationWishes);
    }

    /*목적지찜 전체조회*/
    @Operation(summary = "목적지찜 목록 전체조회")
    @GetMapping
    public ResponseEntity<List<DestinationWish>> getAllDestinationWishes() {
        List<DestinationWish> destinationWishes = destinationWishService.getAllDestinationWishes();
        return ResponseEntity.ok(destinationWishes);
    }
}
