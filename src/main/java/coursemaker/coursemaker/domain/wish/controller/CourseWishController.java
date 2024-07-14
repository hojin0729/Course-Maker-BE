package coursemaker.coursemaker.domain.wish.controller;

import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/coursewishes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "CourseWish", description = "코스 찜 API")
public class CourseWishController {

    private CourseWishService courseWishService;
    private CourseWish courseWish;

    /*코스찜 등록*/
    @Operation(summary = "코스 찜하기")
    @PostMapping
    public ResponseEntity<Void> addCourseWish(@Valid @RequestBody ) {
        List<CourseWish> response = courseWishService.findAllWishes();
        return ResponseEntity.created();
    }

    @PostMapping
    public ResponseEntity<Void> addTag(@Valid @RequestBody TagPostDto request) {
        TagResponseDto response = tagService.createTag(request);

        return ResponseEntity.created(URI.create("/v1/tags/" + response.getId())).build();
    }

    /*코스찜 등록삭제*/


    /*코스찜 전체조회*/
    @Operation(summary = "전체 찜한 코스 조회")
    @GetMapping
    public ResponseEntity<List<CourseWish>> getAllCourseWishes() {
        List<CourseWish> response = courseWishService.findAllWishes();
        return ResponseEntity.ok().body(response);
    }

}
