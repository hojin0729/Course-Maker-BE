package coursemaker.coursemaker.domain.tag.controller;

import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /*전체 태그 조회*/
    @Operation(summary = "전체 태그 조회")
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getTags() {
        List<TagResponseDto> response = tagService.findAllTags()
                .stream()
                .map(Tag::toResponseDto)
                .toList();

        return ResponseEntity.ok().body(response);
    }

    /*태그 생성*/
    @Operation(summary = "태그 생성")
    @ApiResponse(
            responseCode = "201", description = "헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."
    )
    @ApiResponse(
            responseCode = "409", description = "생성하려는 태그의 이름이 이미 있을때 반환합니다.", content = @Content
    )
    @PostMapping
    public ResponseEntity<Void> addTag(@RequestBody TagPostDto request) {
        TagResponseDto response = tagService.createTag(request.toEntity())
                .toResponseDto();

        return ResponseEntity.created(URI.create("/v1/tags/" + response.getId())).build();
    }

    /*태그 수정*/
    @Operation(summary = "태그 수정")
    @ApiResponse(
            responseCode = "200", description = "태그가 제대로 변경됩니다."
    )
    @ApiResponse(
            responseCode = "404", description = "수정하려는 태그의 id를 찾지 못할때 반환합니다.", content = @Content
    )
    @ApiResponse(
            responseCode = "409", description = "수정하려는 태그의 이름이 이미 있을때 반환합니다.", content = @Content
    )
    @PatchMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable(name = "id") Long id
                                                    ,@RequestBody TagPostDto request) {
        Tag tag = request.toEntity();
        tag.setId(id);

        TagResponseDto response = tagService.updateTag(tag)
                .toResponseDto();

        return ResponseEntity.ok().body(response);
    }

    /*태그 삭제*/
    @Operation(summary = "태그 삭제")
    @ApiResponse(
            responseCode = "200", description = "태그가 제대로 삭제됩니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable(name = "id") Long id) {
        tagService.deleteById(id);

        return ResponseEntity.ok().build();
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
