package coursemaker.coursemaker.domain.event.service;

import coursemaker.coursemaker.domain.event.dto.RequestEventDto;
import coursemaker.coursemaker.domain.event.entity.Event;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Event save(RequestEventDto requestEventDto);
    Event update(Long id, RequestEventDto requestEventDto);
    void delete(Long id);
    Event findById(Long id);
    CourseMakerPagination<Event> findAll(Pageable pageable);
}
