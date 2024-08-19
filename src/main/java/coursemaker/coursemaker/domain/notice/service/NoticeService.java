package coursemaker.coursemaker.domain.notice.service;

import coursemaker.coursemaker.domain.notice.dto.RequestNoticeDto;
import coursemaker.coursemaker.domain.notice.entity.Notice;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    // 알리미 등록
    Notice save(RequestNoticeDto requestNoticeDto);
    // 알리미 수정
    Notice update(Long id, RequestNoticeDto requestNoticeDto);
    // 알리미 삭제
    void delete(Long id);
    // 알리미 id값으로 보기
    Notice findById(Long id);
    // 알리미 전체 보기
    CourseMakerPagination<Notice> findAll(Pageable pageable);
}
