package coursemaker.coursemaker.domain.notice.controller;

import coursemaker.coursemaker.domain.notice.dto.RequestNoticeDto;
import coursemaker.coursemaker.domain.notice.dto.ResponseNoticeDto;
import coursemaker.coursemaker.domain.notice.entity.Notice;
import coursemaker.coursemaker.domain.notice.service.NoticeService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("v1/notices")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseNoticeDto> getNoticeById(@PathVariable("id") Long id) {
        Notice notice = noticeService.findById(id);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(notice);
        return ResponseEntity.ok(responseNoticeDto);
    }

    @PostMapping
    public ResponseEntity<ResponseNoticeDto> createNotice(@RequestBody @Valid RequestNoticeDto requestNoticeDto) {
        Notice savedNotice = noticeService.save(requestNoticeDto);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(savedNotice);
        return ResponseEntity.created(URI.create("/v1/notices/" + savedNotice.getId())).body(responseNoticeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseNoticeDto> updateNotice(@PathVariable("id") Long id,
                                                          @RequestBody @Valid RequestNoticeDto requestNoticeDto) {
        Notice updatedNotice = noticeService.update(id, requestNoticeDto);
        ResponseNoticeDto responseNoticeDto = ResponseNoticeDto.toDto(updatedNotice);
        return ResponseEntity.ok(responseNoticeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteNotice(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return ResponseEntity.ok(id);
    }

//    @GetMapping
//    public ResponseEntity<CourseMakerPagination<ResponseNoticeDto>> getAllNotices(Pageable pageable) {
//        CourseMakerPagination<Notice> noticePage = noticeService.findAll(pageable);
//        CourseMakerPagination<ResponseNoticeDto> responseNoticePage = noticePage.map(ResponseNoticeDto::toDto);
//        return ResponseEntity.ok(responseNoticePage);
//    }
}
