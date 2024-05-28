package coursemaker.coursemaker.domain.tag.controller;

import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
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

    /*특정 태그 조회*/
    @Operation(summary = "id에 맞는 태그 조회")
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDto> getTag(@PathVariable(name = "id" ) Long id) {
        TagResponseDto response = tagService.findById(id).toResponseDto();

        return ResponseEntity.ok().body(response);
    }

    /*태그 생성*/
    @Operation(summary = "태그 생성")
    @ApiResponse(responseCode = "201", description = "헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다.")
    @PostMapping
    public ResponseEntity<Void> addTag(@RequestBody TagPostDto request) {
        TagResponseDto response = tagService.createTag(request.toEntity())
                .toResponseDto();

        return ResponseEntity.created(URI.create("/v1/tags/" + response.getId())).build();
    }

    /*태그 수정*/
    @Operation(summary = "태그 수정")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable(name = "id") Long id) {
        tagService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "해당 코스id에 포함된 태그 조회")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<List<TagResponseDto>> getTagsByCourse(@PathVariable(name = "courseId") Long courseId) {
        List<TagResponseDto> response = tagService.findAllByCourseId(courseId)
                .stream()
                .map(Tag::toResponseDto)
                .toList();

        return ResponseEntity.ok().body(response);
    }



}
