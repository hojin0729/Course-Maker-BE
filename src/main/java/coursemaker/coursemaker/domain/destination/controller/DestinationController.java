package coursemaker.coursemaker.domain.destination.controller;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationDuplicatedException;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.exception.IllegalDestinationArgumentException;
import coursemaker.coursemaker.domain.destination.exception.PictureNotFoundException;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.IllegalTagArgumentException;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    @Operation(summary = "전체 여행지 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "전체 여행지 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DestinationDto.class))),
    })
    @Parameter(name = "record", description = "한 페이지 당 표시할 데이터 수")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1 페이지 부터 시작합니다.)")
    /*********스웨거 어노테이션**********/
    // 전체 여행지 목록을 가져옵니다.
    @GetMapping
    public List<DestinationDto> getAllDestinations(@RequestParam(name = "record") int record,
                                                   @RequestParam(name = "page") int page) {
        Pageable pageable = PageRequest.of(page - 1, record);
        Page<Destination> destinations = destinationService.findAll(pageable);
        return destinations.stream()
                .map(destination -> {
                    // Destination의 ID로 관련된 모든 태그를 가져옴.
                    List<TagResponseDto> tags = tagService.findAllByDestinationId(destination.getId())
                            .stream()
                            // 각 태그 엔티티를 TagResponseDto로 변환하고 리스트로 수집.
                            .map(Tag::toResponseDto)
                            .toList();
                    // Destination 엔티티와 태그 리스트를 이용하여 DestinationDto로 변환
                    return DestinationDto.toDto(destination, tags);
                })
                .toList();
    }



    /*********스웨거 어노테이션**********/
    @Operation(summary = "id에 맞는 여행지 상세 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "해당 Id에 맞는 여행지 상세 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = DestinationDto.class))),
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
    @Operation(summary = "여행지 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "생성하려는 태그의 인자값이 올바르지 않을때 반환합니다.", content = @Content),
            @ApiResponse(responseCode = "409", description = "생성하려는 태그의 이름이 이미 있을때 반환합니다.", content = @Content)
    })
    /*********스웨거 어노테이션**********/
    // 여행지를 새로 생성함.
    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@RequestBody RequestDto request) {
        Destination savedDestination = destinationService.save(request);
        List<TagResponseDto> tags = tagService.findAllByDestinationId(savedDestination.getId())
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto response = DestinationDto.toDto(savedDestination, tags);
        return ResponseEntity.created(URI.create("/v1/destination/" + savedDestination.getId())).body(response);
    }




    /*********스웨거 어노테이션**********/
    @Operation(summary = "Id에 해당하는 여행지를 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Id에 해당하는 여행지 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDto.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"nickname\": \"코스메이커\", \"name\": \"부산광역시\", \"tags\": [{\"id\": 1, \"name\": \"애견\", \"description\": \"강아지와 같이 여행하기 너무 좋아요.\"}, {\"id\": 2, \"name\": \"자연\", \"description\": \"자연이 너무 아름다워요.\"}], \"location\": \"부산\", \"latitude\": 30.8968, \"longitude\": -14.5828, \"pictureLink\": \"http://example.com/coursemaker.jpg\", \"content\": \"강아지와 함께 여행을 했는데 주위 자연이 너무 아름다웠어요.\"}"
                            )
                    )),
    })
    @Parameter(name = "id", description = "여행지 Id")
    /*********스웨거 어노테이션**********/
    // Id에 해당하는 여행지의 정보를 수정합니다.
    @PatchMapping("/{id}")
    public ResponseEntity<DestinationDto> updateDestination(@PathVariable("id") Long id,
                                                            @RequestBody  RequestDto request) {
        // 1. id로 여행지를 찾는다.
        Destination destination = destinationService.findById(id);
        if (destination == null) {
            // 2. 여행지가 없으면 404에러를 보낸다.
            return ResponseEntity.notFound().build();
        }
        // 3. Dto를 엔티티로 변환한다.
//        Destination updatedDestination = RequestDto.toEntity(request);
        Destination updatedDestination = request.toEntity();
        // 4. 기존 여행지 id로 설정해서 엔티티 id를 유지한다.
        updatedDestination.setId(destination.getId());
        // 5. 업데이트 된 여행지를 저장한다.
        Destination savedDestination = destinationService.save(request);

        // 6. 요청된 태그리스트가 비어 있지 않으면 태그를 업데이트 한다.
        if (!request.getTags().isEmpty()) {
            List<Long> newTagIds = request.getTags()
                    .stream()
                    .map(TagResponseDto::getId)
                    .toList();
            // 7. 기존 태그를 모두 삭제한다.
            tagService.deleteAllTagByDestination(savedDestination.getId());
            // 8. 새로운 태그를 추가한다.
            tagService.addTagsByDestination(savedDestination.getId(), newTagIds);
        }
        // 9. 업데이트 된 여행지의 태그를 조회해서 dto로 반환한다.
        List<TagResponseDto> updatedTags = tagService.findAllByDestinationId(savedDestination.getId())
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto updatedDto = DestinationDto.toDto(savedDestination, updatedTags);
        // 10. 잘 되면 HTTP 200 OK 응답을 반환한다.
        return ResponseEntity.ok(updatedDto);
    }



    /*********스웨거 어노테이션**********/
    @Operation(summary = "id에 해당하는 여행지 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Id에 해당하는 여행지 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DestinationDto.class))),
    })
    @Parameter(name = "id", description = "여행지 Id")
    // Id에 해당하는 여행지의 정보를 삭제합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteDestinationById(@PathVariable("id") Long id) {

        // 해당 ID의 여행지가 존재하는지 확인합니다.
        Destination destination = destinationService.findById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        // 여행지에 연결된 태그들을 먼저 삭제합니다.
        tagService.deleteAllTagByDestination(id);
        // 여행지 자체를 삭제합니다.
        destinationService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @ExceptionHandler(DestinationDuplicatedException.class)
    public ResponseEntity<String> handleDestinationDuplicatedException(DestinationDuplicatedException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_DESTINATION.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(DestinationNotFoundException.class)
    public ResponseEntity<String> handleDestinationNotFoundException(DestinationNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_DESTINATION.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalDestinationArgumentException.class)
    public ResponseEntity<String> handleIllegalDestinationArgumentException(IllegalDestinationArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.ILLEGAL_DESTINATION_ARGUMENT.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<String> handlePictureNotFoundException(PictureNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.PICTURE_NOT_FOUND.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(TagDuplicatedException.class)
    public ResponseEntity<String> handleTagDuplicatedException(TagDuplicatedException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_TAG.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(TagNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_TAG.getStatus())
                .body(e.getMessage());
    }
}
