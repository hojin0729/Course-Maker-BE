package coursemaker.coursemaker.domain.review.controller;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.auth.exception.LoginRequiredException;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.dto.ResponseDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;

import coursemaker.coursemaker.domain.review.service.OrderBy;
import coursemaker.coursemaker.exception.ErrorResponse;
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
            @ApiResponse(responseCode = "200", description = "해당 ID에 맞는 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID에 맞는 리뷰를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> getDestinationReviewById(@PathVariable("id") Long id,
                                                                           @AuthenticationPrincipal LoginedInfo logined) {
        DestinationReview destinationReview = destinationReviewService.findById(id);
        Boolean isMyDestinationReview = logined != null && logined.getNickname().equals(destinationReview.getMember().getNickname());
        Boolean isMyLikeReview = logined != null && destinationReviewService.isReviewRecommendedByUser(id, logined.getNickname());
        Destination destination = destinationService.findById(destinationReview.getDestination().getId());
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, destinationReview, isMyDestinationReview, isMyLikeReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @PostMapping
    @Operation(summary = "새 목적지 리뷰 작성", description = "새 목적지 리뷰를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰가 성공적으로 생성되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "생성하려는 리뷰의 인자값이 올바르지 않을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
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
    @Parameter(name = "destinationId", description = "리뷰를 작성할 여행지의 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> createDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @AuthenticationPrincipal LoginedInfo logined) {

        if(logined == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[DestinationReview] 리뷰 생성 실패");
        }

        String nickname = logined.getNickname();
        requestDestinationDto.setNickname(nickname);

        DestinationReview savedDestinationReview = destinationReviewService.save(requestDestinationDto, destinationId);
        Boolean isMyDestinationReview = logined.getNickname().equals(savedDestinationReview.getMember().getNickname());
        Destination destination = destinationService.findById(destinationId);
        Boolean isMyLikeReview = false;
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, savedDestinationReview, isMyDestinationReview, isMyLikeReview);
        return ResponseEntity.created(URI.create("/v1/destinationreview/" + savedDestinationReview.getId())).body(responseDestinationDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "목적지 리뷰 업데이트", description = "기존 목적지 리뷰를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID에 해당하는 리뷰 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "400", description = "수정하려는 리뷰의 인자값이 올바르지 않을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"접근 권한이 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "수정하려는 리뷰의 ID를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<ResponseDestinationDto> updateDestinationReview(@RequestBody @Valid RequestDestinationDto requestDestinationDto,
                                                                          @RequestParam(name = "destinationId") Long destinationId,
                                                                          @AuthenticationPrincipal LoginedInfo logined) {
        if(logined == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[DestinationReview] 리뷰 생성 실패");
        }

        String nickname = logined.getNickname();
        requestDestinationDto.setNickname(nickname);

        DestinationReview updatedDestinationReview = destinationReviewService.update(destinationId, requestDestinationDto, nickname);
        Destination destination = destinationService.findById(destinationId);
        Boolean isMyDestinationReview = logined.getNickname().equals(updatedDestinationReview.getMember().getNickname());
        Boolean isMyLikeReview = destinationReviewService.isReviewRecommendedByUser(updatedDestinationReview.getId(), logined.getNickname());
        ResponseDestinationDto responseDestinationDto = ResponseDestinationDto.toDto(destination, updatedDestinationReview, isMyDestinationReview, isMyLikeReview);
        return ResponseEntity.ok(responseDestinationDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "목적지 리뷰 삭제", description = "특정 목적지 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID에 해당하는 리뷰 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인 후 이용이 가능합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용이 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 403, \"errorType\": \"Forbidden\", \"message\": \"접근 권한이 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "삭제하려는 리뷰의 ID를 찾지 못할 때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "id", description = "리뷰 ID", required = true, example = "1")
    public ResponseEntity<Long> deleteDestinationReview(@PathVariable("id") Long id, @AuthenticationPrincipal LoginedInfo logined) {
        if(logined == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[DestinationReview] 리뷰 삭제 실패");
        }

        // 해당 ID의 리뷰가 존재하는지 확인합니다.
        DestinationReview destinationReview = destinationReviewService.findById(id);
        if (destinationReview == null) {
            return ResponseEntity.notFound().build();
        }

        // 로그인한 사용자 닉네임 가져오기
        String nickname = logined.getNickname();
        // 해당 리뷰가 로그인한 사용자에게 속하는지 확인합니다.
        if (!destinationReview.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("작성자와 정보가 일치하지 않습니다.", "사용자가 이 자원에 접근할 권한이 없습니다.");
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
    @Parameter(name = "orderBy", description = "정렬 기준 (NEWEST: 최신순, RECOMMEND: 추천순, RATING DOWN: 별점 낮은 순, RATING UP: 별점 높은 순 중 하나)", example = "NEWEST")
    @GetMapping
    public ResponseEntity<CourseMakerPagination<ResponseDestinationDto>> getAllDestinationReviews(
            @RequestParam(name = "destinationId") Long destinationId,
            @RequestParam(defaultValue = "20", name = "record") int record,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(name = "orderBy", defaultValue = "NEWEST") OrderBy orderBy,
            @AuthenticationPrincipal LoginedInfo logined) {

        Pageable pageable = PageRequest.of(page - 1, record);

        CourseMakerPagination<DestinationReview> reviewPage = destinationReviewService.findAllByDestinationId(destinationId, pageable, orderBy);
        List<DestinationReview> reviewList = reviewPage.getContents();

        List<ResponseDestinationDto> responseDtos = reviewList.stream()
                .map(review -> {
                    Destination destination = destinationService.findById(review.getDestination().getId());
                    Boolean isMyDestinationReview = logined != null && logined.getNickname().equals(review.getMember().getNickname());
                    Boolean isMyLikeReview = logined != null && destinationReviewService.isReviewRecommendedByUser(review.getId(), logined.getNickname());
                    return ResponseDestinationDto.toDto(destination, review, isMyDestinationReview, isMyLikeReview);
                })
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseDestinationDto> responseReviewPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(responseDtos, pageable, reviewPage.getTotalContents()),
                reviewPage.getTotalContents()
        );

        return ResponseEntity.ok(responseReviewPage);
    }
    @Operation(summary = "닉네임으로 여행지 리뷰 조회", description = "특정 사용자의 닉네임을 통해 해당 사용자가 작성한 여행지 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 닉네임으로 작성된 리뷰를 찾지 못할 때 반환", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 닉네임으로 작성된 리뷰가 없습니다.\"}"
                    )
            ))
    })
    @Parameter(name = "nickname", description = "리뷰를 조회할 사용자의 닉네임", required = true)
    @Parameter(name = "record", description = "한 페이지당 표시할 데이터 수", example = "20")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1부터 시작)", example = "1")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<CourseMakerPagination<ResponseDestinationDto>> findDestinationReviewByNickname(
            @PathVariable("nickname") String nickname,
            @RequestParam(defaultValue = "20", name = "record") Integer record,
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @AuthenticationPrincipal LoginedInfo logined) {

        Pageable pageable = PageRequest.of(page - 1, record);
        CourseMakerPagination<DestinationReview> destinationReviewPage = destinationReviewService.findByMemberNickname(nickname, pageable);

        List<ResponseDestinationDto> contents = destinationReviewPage.getContents().stream()
                .map(destinationReview -> {
                    Boolean isMyDestinationReview = logined != null && logined.getNickname().equals(destinationReview.getMember().getNickname());
                    Boolean isMyLikeReview = logined != null && destinationReviewService.isReviewRecommendedByUser(destinationReview.getId(), logined.getNickname());
                    Destination destination = destinationService.findById(destinationReview.getDestination().getId());
                    return ResponseDestinationDto.toDto(destination, destinationReview, isMyDestinationReview, isMyLikeReview);
                })
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseDestinationDto> responseReviewPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(contents, pageable, destinationReviewPage.getTotalContents()),
                destinationReviewPage.getTotalContents()
        );

        return ResponseEntity.ok(responseReviewPage);
    }

    @PostMapping("/{id}/recommend")
    @Operation(summary = "리뷰 추천 추가", description = "리뷰에 대해 추천(좋아요)를 추가합니다. 로그인된 사용자만 이용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천이 성공적으로 추가되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요한 요청입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "해당 리뷰가 존재하지 않습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    public ResponseEntity<ResponseDestinationDto> addRecommend(@PathVariable("id") Long id, @AuthenticationPrincipal LoginedInfo logined) {
        if (logined == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[DestinationReview] 추천 실패");
        }

        destinationReviewService.addRecommend(id, logined.getNickname());
        DestinationReview updatedReview = destinationReviewService.findById(id);
        Destination destination = destinationService.findById(updatedReview.getDestination().getId());
        Boolean isMyDestinationReview = logined.getNickname().equals(updatedReview.getMember().getNickname());
        Boolean isMyLikeReview = true;

        ResponseDestinationDto responseDto = ResponseDestinationDto.toDto(destination, updatedReview, isMyDestinationReview, isMyLikeReview);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/{id}/unrecommend")
    @Operation(summary = "리뷰 추천 취소", description = "리뷰에 대해 추가된 추천(좋아요)를 취소합니다. 로그인된 사용자만 이용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천이 성공적으로 취소되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDestinationDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요한 요청입니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 401, \"errorType\": \"login required\", \"message\": \"로그인 후 이용 가능합니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "해당 리뷰가 존재하지 않습니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 리뷰가 없습니다.\"}"
                    )
            ))
    })
    public ResponseEntity<ResponseDestinationDto> removeRecommend(@PathVariable("id") Long id, @AuthenticationPrincipal LoginedInfo logined) {
        if (logined == null) {
            throw new LoginRequiredException("로그인 후 이용 가능합니다.", "[DestinationReview] 추천 취소 실패");
        }

        destinationReviewService.removeRecommend(id, logined.getNickname());
        DestinationReview updatedReview = destinationReviewService.findById(id);
        Destination destination = destinationService.findById(updatedReview.getDestination().getId());
        Boolean isMyDestinationReview = logined.getNickname().equals(updatedReview.getMember().getNickname());
        Boolean isMyLikeReview = false;

        ResponseDestinationDto responseDto = ResponseDestinationDto.toDto(destination, updatedReview, isMyDestinationReview, isMyLikeReview);
        return ResponseEntity.ok(responseDto);
    }

}
