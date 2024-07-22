package coursemaker.coursemaker.domain.event.service;

import coursemaker.coursemaker.domain.event.dto.RequestEventDto;
import coursemaker.coursemaker.domain.event.entity.Event;
import coursemaker.coursemaker.domain.event.repository.EventRepository;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(RequestEventDto requestEventDto) {
        Event event = requestEventDto.toEntity();
        return eventRepository.save(event);
    }

    @Override
    public Event update(Long id, RequestEventDto requestEventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. id: " + id));

        existingEvent.setTitle(requestEventDto.getTitle());
        existingEvent.setPicture(requestEventDto.getPicture());
        existingEvent.setShort_description(requestEventDto.getShortDescription());
        existingEvent.setDescription(requestEventDto.getDescription());

        return eventRepository.save(existingEvent);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. id: " + id));
    }

    @Override
    public CourseMakerPagination<Event> findAll(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }
}
