package coursemaker.coursemaker.domain.event.service;

import coursemaker.coursemaker.domain.event.dto.RequestEventDto;
import coursemaker.coursemaker.domain.event.entity.Event;
import coursemaker.coursemaker.domain.event.repository.EventRepository;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(RequestEventDto requestEventDto) {
        log.info("[Event] 이벤트 저장 시작 - 제목: {}", requestEventDto.getTitle());
        Event event = requestEventDto.toEntity();
        Event savedEvent = eventRepository.save(event);
        log.info("[Event] 이벤트 저장 완료 - ID: {}, 제목: {}", savedEvent.getId(), savedEvent.getTitle());
        return savedEvent;
    }

    @Override
    public Event update(Long id, RequestEventDto requestEventDto) {
        log.info("[Event] 이벤트 업데이트 시작 - ID: {}", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Event] 이벤트 찾기 실패 - ID: {}", id);
                    return new IllegalArgumentException("이벤트를 찾을 수 없습니다. id: " + id);
                });

        existingEvent.setTitle(requestEventDto.getTitle());
        existingEvent.setPicture(requestEventDto.getPicture());
        existingEvent.setShort_description(requestEventDto.getShortDescription());
        existingEvent.setDescription(requestEventDto.getDescription());

        Event updatedEvent = eventRepository.save(existingEvent);
        log.info("[Event] 이벤트 업데이트 완료 - ID: {}, 제목: {}", updatedEvent.getId(), updatedEvent.getTitle());

        return updatedEvent;
    }

    @Override
    public void delete(Long id) {
        log.info("[Event] 이벤트 삭제 시작 - ID: {}", id);
        eventRepository.deleteById(id);
        log.info("[Event] 이벤트 삭제 완료 - ID: {}", id);
    }

    @Override
    public Event findById(Long id) {
        log.info("[Event] 이벤트 조회 시작 - ID: {}", id);
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Event] 이벤트 찾기 실패 - ID: {}", id);
                    return new IllegalArgumentException("이벤트를 찾을 수 없습니다. id: " + id);
                });
    }

    @Override
    public CourseMakerPagination<Event> findAll(Pageable pageable) {
        log.info("[Event] 이벤트 목록 조회 시작");
        Page<Event> page = eventRepository.findAll(pageable);
        log.info("[Event] 이벤트 목록 조회 완료 - 총 개수: {}", page.getTotalElements());
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }
}
