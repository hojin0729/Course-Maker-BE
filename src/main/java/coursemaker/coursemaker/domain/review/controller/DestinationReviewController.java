package coursemaker.coursemaker.domain.review.controller;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.dto.ResponseDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;
import coursemaker.coursemaker.util.LoginUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("v1/destinationreview")
public class DestinationReviewController {
    private final DestinationReviewService destinationReviewService;
    private final DestinationService destinationService;

    public DestinationReviewController(DestinationReviewService destinationReviewService, DestinationService destinationService) {
        this.destinationReviewService = destinationReviewService;
        this.destinationService = destinationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDestinationDto> getDestinationReviewById(@PathVariable("id") Long id) {
        DestinationReview destinationReview = destinationReviewService.findById(id);
        Destination destination = destinationService.findById(destinationReview.getDestination().getId());
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, destinationReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @PostMapping
    public ResponseEntity<ResponseDestinationDto> createDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @LoginUser String nickname) {
        requestDestinationDto.setNickname(nickname);
        DestinationReview savedDestinationReview = destinationReviewService.save(requestDestinationDto, destinationId);
        Destination destination = destinationService.findById(destinationId);
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, savedDestinationReview);
        return ResponseEntity.created(URI.create("/v1/destinationreview/" + savedDestinationReview.getId())).body(responseDestinationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDestinationDto> updateDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @LoginUser String nickname) {
        DestinationReview updatedDestinationReview = destinationReviewService.update(destinationId, requestDestinationDto, nickname);
        Destination destination = destinationService.findById(destinationId);
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, updatedDestinationReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteDestinationReview(@PathVariable("id") Long id, @LoginUser String nickname) {
        // 해당 ID의 리뷰가 존재하는지 확인합니다.
        DestinationReview destinationReview = destinationReviewService.findById(id);
        if (destinationReview == null) {
            return ResponseEntity.notFound().build();
        }
        // 해당 리뷰가 로그인한 사용자에게 속하는지 확인합니다.
        if (!destinationReview.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("Forbidden", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        // 리뷰를 삭제합니다.
        destinationReviewService.delete(id);
        return ResponseEntity.ok(id);
    }
}