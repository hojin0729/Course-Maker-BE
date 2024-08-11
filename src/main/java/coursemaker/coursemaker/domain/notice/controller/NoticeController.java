package coursemaker.coursemaker.domain.notice.controller;

import coursemaker.coursemaker.domain.notice.dto.RequestNoticeDto;
import coursemaker.coursemaker.domain.notice.dto.ResponseNoticeDto;
import coursemaker.coursemaker.domain.notice.entity.Notice;
import coursemaker.coursemaker.domain.notice.service.NoticeService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/notices")
@Tag(name = "Notice", description = "공지사항 관리 API")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "공지사항 ID로 조회", description = "특정 ID의 공지사항을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseNoticeDto> getNoticeById(@PathVariable("id") Long id) {
        Notice notice = noticeService.findById(id);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(notice);
        return ResponseEntity.ok(responseNoticeDto);
    }

    @Operation(summary = "새 공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @PostMapping
    public ResponseEntity<ResponseNoticeDto> createNotice(@RequestBody @Valid RequestNoticeDto requestNoticeDto) {
        Notice savedNotice = noticeService.save(requestNoticeDto);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(savedNotice);
        return ResponseEntity.created(URI.create("/v1/notices/" + savedNotice.getId())).body(responseNoticeDto);
    }

    @Operation(summary = "공지사항 업데이트", description = "기존 공지사항을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseNoticeDto> updateNotice(@PathVariable("id") Long id,
                                                          @RequestBody @Valid RequestNoticeDto requestNoticeDto) {
        Notice updatedNotice = noticeService.update(id, requestNoticeDto);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(updatedNotice);
        return ResponseEntity.ok(responseNoticeDto);
    }

    @Operation(summary = "공지사항 삭제", description = "특정 ID의 공지사항을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteNotice(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "공지사항 목록 페이지네이션 조회", description = "페이지네이션을 사용하여 공지사항 목록을 조회합니다.")
    @Parameter(name = "record", description = "페이지당 표시할 공지사항 수")
    @Parameter(name = "page", description = "조회할 페이지 번호 (1부터 시작)")
    @GetMapping
    public ResponseEntity<CourseMakerPagination<ResponseNoticeDto>> getAllNotices(
            @RequestParam(defaultValue = "20", name = "record") int record,
            @RequestParam(defaultValue = "1", name = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, record);

        CourseMakerPagination<Notice> noticePage = noticeService.findAll(pageable);
        List<Notice> noticeList = noticePage.getContents();

        List<ResponseNoticeDto> responseDtos = noticeList.stream()
                .map(ResponseNoticeDto::toDto)
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseNoticeDto> responseNoticePage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(responseDtos, pageable, noticePage.getTotalContents()),
                noticePage.getTotalContents()
        );

        return ResponseEntity.ok(responseNoticePage);
    }
}
