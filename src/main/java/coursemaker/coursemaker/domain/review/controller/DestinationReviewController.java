package coursemaker.coursemaker.domain.review.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.dto.ResponseDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/destinationreview")
@io.swagger.v3.oas.annotations.tags.Tag(name = "DestinationReview", description = "목적지 리뷰 API")
public class DestinationReviewController {
    private final DestinationReviewService destinationReviewService;
    private final DestinationService destinationService;

    public DestinationReviewController(DestinationReviewService destinationReviewService, DestinationService destinationService) {
        this.destinationReviewService = destinationReviewService;
        this.destinationService = destinationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "리뷰 ID로 목적지 리뷰 가져오기", description = "리뷰 ID를 사용하여 특정 목적지 리뷰를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID에 맞는 리뷰를 찾을 수 없음", content = @Content)
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> getDestinationReviewById(@PathVariable("id") Long id) {
        DestinationReview destinationReview = destinationReviewService.findById(id);
        Destination destination = destinationService.findById(destinationReview.getDestination().getId());
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, destinationReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @PostMapping
    @Operation(summary = "새 목적지 리뷰 작성", description = "새 목적지 리뷰를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 생성 성공", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content)
    })
    @Parameter(name = "destinationId", description = "리뷰를 작성할 여행지의 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> createDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @AuthenticationPrincipal LoginedInfo logined) {
        // 로그인한 사용자 닉네임 가져오기
        String nickname = logined.getNickname();
        requestDestinationDto.setNickname(nickname);

        DestinationReview savedDestinationReview = destinationReviewService.save(requestDestinationDto, destinationId);
        Destination destination = destinationService.findById(destinationId);
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, savedDestinationReview);
        return ResponseEntity.created(URI.create("/v1/destinationreview/" + savedDestinationReview.getId())).body(responseDestinationDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "목적지 리뷰 업데이트", description = "기존 목적지 리뷰를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 업데이트 성공", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content)
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> updateDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @AuthenticationPrincipal LoginedInfo logined) {
        // 로그인한 사용자 닉네임 가져오기
        String nickname = logined.getNickname();
        requestDestinationDto.setNickname(nickname);

        DestinationReview updatedDestinationReview = destinationReviewService.update(destinationId, requestDestinationDto, nickname);
        Destination destination = destinationService.findById(destinationId);
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, updatedDestinationReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "목적지 리뷰 삭제", description = "특정 목적지 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content)
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<Long> deleteDestinationReview(@PathVariable("id") Long id, @AuthenticationPrincipal LoginedInfo logined) {
        // 해당 ID의 리뷰가 존재하는지 확인합니다.
        DestinationReview destinationReview = destinationReviewService.findById(id);
        if (destinationReview == null) {
            return ResponseEntity.notFound().build();
        }

        // 로그인한 사용자 닉네임 가져오기
        String nickname = logined.getNickname();
        // 해당 리뷰가 로그인한 사용자에게 속하는지 확인합니다.
        if (!destinationReview.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("Forbidden", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        // 리뷰를 삭제합니다.
        destinationReviewService.delete(id);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "목적지 리뷰 페이지네이션 조회", description = "페이지네이션을 사용하여 특정 목적지에 대한 리뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공", content = @Content(schema = @Schema(implementation = CourseMakerPagination.class)))
    })
    @Parameter(name = "destinationId", description = "리뷰를 조회할 여행지의 ID", required = true, example = "1")
    @Parameter(name = "record", description = "페이지당 표시할 데이터 수", example = "20")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1부터 시작합니다.)", example = "1")
    @GetMapping
    public ResponseEntity<CourseMakerPagination<ResponseDestinationDto>> getAllDestinationReviews(
            @RequestParam(name = "destinationId") Long destinationId,
            @RequestParam(defaultValue = "20", name = "record") int record,
            @RequestParam(defaultValue = "1", name = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, record);

        CourseMakerPagination<DestinationReview> reviewPage = destinationReviewService.findAllByDestinationId(destinationId, pageable);
        List<DestinationReview> reviewList = reviewPage.getContents();

        List<ResponseDestinationDto> responseDtos = reviewList.stream()
                .map(review -> {
                    Destination destination = destinationService.findById(review.getDestination().getId());
                    return ResponseDestinationDto.toDto(destination, review);
                })
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseDestinationDto> responseReviewPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(responseDtos, pageable, reviewPage.getTotalContents()),
                reviewPage.getTotalContents()
        );

        return ResponseEntity.ok(responseReviewPage);
    }
}