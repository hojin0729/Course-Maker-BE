package coursemaker.coursemaker.domain.destination.controller;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.*;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.OrderBy;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import coursemaker.coursemaker.util.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static coursemaker.coursemaker.domain.member.entity.QMember.member;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Destination", description = "여행지 API")
@RestController
@RequestMapping("v1/destination")
public class DestinationController {
    private final DestinationService destinationService;
    private final TagService tagService;

    public DestinationController(DestinationService destinationService, TagService tagService) {
        this.destinationService = destinationService;
        this.tagService = tagService;
    }

    /*********스웨거 어노테이션**********/
    @Operation(summary = "전체 여행지 목록 조회", description = "한 페이지에 표시할 데이터 수(record)와 조회할 페이지 번호(page)를 입력하여 전체 여행지 목록을 조회합니다. 페이지 번호는 1부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "전체 여행지 목록 조회 성공"
            ),
    })
    @Parameter(name = "tagIds", description = "필터링할 태그 ID 목록(선택 안할 시 전체 태그 조회)", example = "[1, 2, 3]")
    @Parameter(name = "record", description = "한 페이지 당 표시할 데이터 수")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1 페이지 부터 시작합니다.)")
    @Parameter(name = "orderBy", description = "정렬 기준 (VIEWS: 조회수, NEWEST: 최신순, POPULAR: 인기순, RATING: 평점순 중 하나)", example = "NEWEST")
    /*********스웨거 어노테이션**********/
    // 전체 여행지 목록을 가져옵니다.
    @GetMapping
    public ResponseEntity<CourseMakerPagination<DestinationDto>> getAllDestinations(@RequestParam(name = "tagIds", required = false) List<Long> tagIds,
                                                                                    @RequestParam(defaultValue = "20", name = "record") int record,
                                                                                    @RequestParam(defaultValue = "1", name = "page") int page,
                                                                                    @RequestParam(name = "orderBy", defaultValue = "NEWEST") OrderBy orderBy) {
        Pageable pageable = PageRequest.of(page - 1, record);

        List<DestinationDto> destinationDtos = new ArrayList<>();
        CourseMakerPagination<Destination> destinations = tagService.findAllDestinationByTagIds(tagIds, pageable, orderBy);
        int totalPage = destinations.getTotalPage();
        List<Destination> destinationList = destinations.getContents();

        // 각 Destination 엔티티를 DestinationDto로 변환
        for (Destination destination : destinationList) {
            List<TagResponseDto> tags = tagService.findAllByDestinationId(destination.getId())
                    .stream()
                    .map(Tag::toResponseDto)
                    .toList();
            destinationDtos.add(DestinationDto.toDto(destination, tags));
        }

        Page<DestinationDto> responsePage = new PageImpl<>(destinationDtos, pageable, totalPage);

        CourseMakerPagination<DestinationDto> response = new CourseMakerPagination<>(pageable, responsePage);

        return ResponseEntity.ok(response);
    }



    /*********스웨거 어노테이션**********/
    @Operation(summary = "id에 맞는 여행지 상세 정보 조회", description = "여행지 ID를 입력하여 해당 여행지의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "해당 Id에 맞는 여행지 상세 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = DestinationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "해당 Id에 맞는 여행지를 찾지 못할 때 반환합니다.", content = @Content)
    })
    @Parameter(name = "id", description = "여행지 Id")
    /*********스웨거 어노테이션**********/
    // id에 해당하는 여행지의 상세 정보를 가져옴.
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable("id") Long id) {
        Destination destination = destinationService.findById(id);
        List<TagResponseDto> tags = tagService.findAllByDestinationId(id)
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto destinationDto = DestinationDto.toDto(destination, tags);
        return ResponseEntity.ok(destinationDto);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "여행지 생성", description = "새로운 여행지 정보를 입력하여 여행지를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "여행지가 성공적으로 생성되었습니다. 헤더의 Location 필드에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),

            @ApiResponse(responseCode = "400", description = "생성하려는 여행지의 인자값이 올바르지 않을때 반환합니다.", content = @Content),

            @ApiResponse(responseCode = "409", description = "생성하려는 여행지의 이름이 이미 있을때 반환합니다.", content = @Content)
    })
    /*********스웨거 어노테이션**********/
    // 여행지를 새로 생성함.
    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@Valid @RequestBody RequestDto request, @LoginUser String nickname) {
        request.setNickname(nickname);
        Destination savedDestination = destinationService.save(request);
        List<TagResponseDto> tags = tagService.findAllByDestinationId(savedDestination.getId())
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto response = DestinationDto.toDto(savedDestination, tags);
        return ResponseEntity.created(URI.create("/v1/destination/" + savedDestination.getId())).body(response);
    }


    /*********스웨거 어노테이션**********/
    @Operation(summary = "id에 해당하는 여행지 수정", description = "여행지 ID와 수정할 정보를 입력하여 해당 여행지의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Id에 해당하는 여행지 수정 성공",
                    content = @Content(schema = @Schema(implementation = DestinationDto.class))),
            @ApiResponse(responseCode = "400", description = "수정하려는 여행지의 인자값이 올바르지 않을 때 반환합니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "수정하려는 여행지의 id를 찾지 못할 때 반환합니다.", content = @Content),
            @ApiResponse(responseCode = "409", description = "수정하려는 여행지의 이름이 이미 있을 때 반환합니다.", content = @Content)
    })
    @Parameter(name = "id", description = "여행지 Id")
    /*********스웨거 어노테이션**********/
    // Id에 해당하는 여행지의 정보를 수정합니다.
    @PatchMapping("/{id}")

    public ResponseEntity<DestinationDto> updateDestination(@PathVariable("id") Long id, @Valid @RequestBody RequestDto request, @LoginUser String nickname) {
        request.setNickname(nickname);
        // 해당 여행지가 로그인한 사용자에게 속하는지 확인
        Destination existingDestination = destinationService.findById(id);
        if (!existingDestination.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("Forbidden", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        Destination updatedDestination = destinationService.update(id, request);
        List<TagResponseDto> updatedTags = tagService.findAllByDestinationId(updatedDestination.getId())
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto updatedDto = DestinationDto.toDto(updatedDestination, updatedTags);
        return ResponseEntity.ok(updatedDto);
    }



    /*********스웨거 어노테이션**********/
    @Operation(summary = "id에 해당하는 여행지 삭제", description = "여행지 ID를 입력하여 해당 여행지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Id에 해당하는 여행지 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없을 때 반환합니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "삭제하려는 여행지의 id를 찾지 못할 때 반환합니다.", content = @Content)
    })
    @Parameter(name = "id", description = "여행지 Id")
    // Id에 해당하는 여행지의 정보를 삭제합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteDestinationById(@PathVariable("id") Long id, @LoginUser String nickname) {

        // 해당 ID의 여행지가 존재하는지 확인합니다.
        Destination destination = destinationService.findById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        // 해당 여행지가 로그인한 사용자에게 속하는지 확인
        if (!destination.getMember().getNickname().equals(nickname)) {
            throw new ForbiddenException("Forbidden", "사용자가 이 자원에 접근할 권한이 없습니다.");
        }
        // 여행지에 연결된 태그들을 먼저 삭제합니다.
        tagService.deleteAllTagByDestination(id);
        // 여행지 자체를 삭제합니다.
        destinationService.deleteById(id);
        return ResponseEntity.ok(id);
    }
}