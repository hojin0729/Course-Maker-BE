package coursemaker.coursemaker.domain.tag.controller;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.tag.dto.TagUpdateDto;
import coursemaker.coursemaker.domain.tag.service.OrderBy;
import coursemaker.coursemaker.util.CourseMakerPagination;
import coursemaker.coursemaker.util.LoginUser;
import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.IllegalTagArgumentException;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "태그 API")
public class TagController {

    private final TagService tagService;

    /*전체 태그 조회*/
    @Operation(summary = "전체 태그 조회")
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getTags() {
        List<TagResponseDto> response = tagService.findAllTags();
        return ResponseEntity.ok().body(response);
    }

    /*태그 생성*/
    @Operation(summary = "태그 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "헤더의 location에 생성된 데이터에 접근할 수 있는 주소를 반환합니다."),
            @ApiResponse(responseCode = "400", description = "생성하려는 태그의 인자값이 올바르지 않을때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"태그 이름은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "생성하려는 태그의 이름이 이미 있을때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"태그 이름이 이미 존재합니다.\"}"
                    )
            ))
    })
    @PostMapping
    public ResponseEntity<Void> addTag(@Valid @RequestBody TagPostDto request) {
        TagResponseDto response = tagService.createTag(request);

        return ResponseEntity.created(URI.create("/v1/tags/" + response.getId())).build();
    }

    /*태그 수정*/
    @Operation(summary = "태그 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그가 제대로 변경됩니다."),
            @ApiResponse(responseCode = "400", description = "수정하려는 태그의 인자값이 올바르지 않을때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"errorType\": \"Illegal argument\", \"message\": \"태그 이름은 공백 혹은 빈 문자는 허용하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "수정하려는 태그의 id를 찾지 못할때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 태그가 없습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "409", description = "수정하려는 태그의 이름이 이미 있을때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 409, \"errorType\": \"Duplicated item\", \"message\": \"태그 이름이 이미 존재합니다.\"}"
                    )
            ))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable(name = "id") Long id
                                                    ,@Valid @RequestBody TagUpdateDto request) {

        TagResponseDto response = tagService.updateTag(request);

        return ResponseEntity.ok().body(response);
    }

    /*태그 삭제*/
    @Operation(summary = "태그 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그가 제대로 삭제됩니다."),
            @ApiResponse(responseCode = "404", description = "삭제하려는 태그의 id를 찾지 못할때 반환합니다.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"errorType\": \"Invalid item\", \"message\": \"해당하는 태그가 없습니다.\"}"
                    )
            ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable(name = "id") Long id) {
        tagService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
