package coursemaker.coursemaker.domain.notice.service;

import coursemaker.coursemaker.domain.notice.dto.RequestNoticeDto;
import coursemaker.coursemaker.domain.notice.entity.Notice;
import coursemaker.coursemaker.domain.notice.repository.NoticeRepository;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public Notice save(RequestNoticeDto requestNoticeDto) {
        Notice notice = requestNoticeDto.toEntity();
        return noticeRepository.save(notice);
    }

    @Override
    public Notice update(Long id, RequestNoticeDto requestNoticeDto) {
        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다. id: " + id));

        existingNotice.setTitle(requestNoticeDto.getTitle());
        existingNotice.setDescription(requestNoticeDto.getDescription());
        existingNotice.setPicture(requestNoticeDto.getPicture());

        return noticeRepository.save(existingNotice);
    }

    @Override
    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다. id: " + id));
    }

    @Override
    public CourseMakerPagination<Notice> findAll(Pageable pageable) {
        Page<Notice> page = noticeRepository.findAll(pageable);
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }
}
