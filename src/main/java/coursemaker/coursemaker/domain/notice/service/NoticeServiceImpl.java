package coursemaker.coursemaker.domain.notice.service;

import coursemaker.coursemaker.domain.notice.dto.RequestNoticeDto;
import coursemaker.coursemaker.domain.notice.entity.Notice;
import coursemaker.coursemaker.domain.notice.repository.NoticeRepository;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    @Override
    public Notice save(RequestNoticeDto requestNoticeDto) {
        log.info("[Notice] 공지사항 저장 시작 - 제목: {}", requestNoticeDto.getTitle());
        Notice notice = requestNoticeDto.toEntity();
        Notice savedNotice = noticeRepository.save(notice);
        log.info("[Notice] 공지사항 저장 완료 - ID: {}, 제목: {}", savedNotice.getId(), savedNotice.getTitle());
        return savedNotice;
    }

    @Transactional
    @Override
    public Notice update(Long id, RequestNoticeDto requestNoticeDto) {
        log.info("[Notice] 공지사항 업데이트 시작 - ID: {}", id);

        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Notice] 공지사항 찾기 실패 - ID: {}", id);
                    return new IllegalArgumentException("알림을 찾을 수 없습니다. id: " + id);
                });

        existingNotice.setTitle(requestNoticeDto.getTitle());
        existingNotice.setDescription(requestNoticeDto.getDescription());
        existingNotice.setPicture(requestNoticeDto.getPicture());

        Notice updatedNotice = noticeRepository.save(existingNotice);
        log.info("[Notice] 공지사항 업데이트 완료 - ID: {}, 제목: {}", updatedNotice.getId(), updatedNotice.getTitle());

        return updatedNotice;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("[Notice] 공지사항 삭제 시작 - ID: {}", id);
        noticeRepository.deleteById(id);
        log.info("[Notice] 공지사항 삭제 완료 - ID: {}", id);
    }

    @Override
    public Notice findById(Long id) {
        log.info("[Notice] 공지사항 조회 시작 - ID: {}", id);
        return noticeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Notice] 공지사항 찾기 실패 - ID: {}", id);
                    return new IllegalArgumentException("알림을 찾을 수 없습니다. id: " + id);
                });
    }

    @Override
    public CourseMakerPagination<Notice> findAll(Pageable pageable) {
        log.info("[Notice] 공지사항 목록 조회 시작");
        Page<Notice> page = noticeRepository.findAll(pageable);
        log.info("[Notice] 공지사항 목록 조회 완료 - 총 개수: {}", page.getTotalElements());
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }
}
