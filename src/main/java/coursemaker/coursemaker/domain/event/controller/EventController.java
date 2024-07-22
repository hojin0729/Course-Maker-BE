package coursemaker.coursemaker.domain.event.controller;

import coursemaker.coursemaker.domain.event.dto.RequestEventDto;
import coursemaker.coursemaker.domain.event.dto.ResponseEventDto;
import coursemaker.coursemaker.domain.event.entity.Event;
import coursemaker.coursemaker.domain.event.service.EventService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEventDto> getEventById(@PathVariable("id") Long id) {
        Event event = eventService.findById(id);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(event);
        return ResponseEntity.ok(responseEventDto);
    }

    @PostMapping
    public ResponseEntity<ResponseEventDto> createEvent(@RequestBody @Valid RequestEventDto requestEventDto) {
        Event savedEvent = eventService.save(requestEventDto);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(savedEvent);
        return ResponseEntity.created(URI.create("/v1/events/" + savedEvent.getId())).body(responseEventDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEventDto> updateEvent(@PathVariable("id") Long id,
                                                        @RequestBody @Valid RequestEventDto requestEventDto) {
        Event updatedEvent = eventService.update(id, requestEventDto);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(updatedEvent);
        return ResponseEntity.ok(responseEventDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteEvent(@PathVariable("id") Long id) {
        eventService.delete(id);
        return ResponseEntity.ok(id);
    }

//    @GetMapping
//    public ResponseEntity<CourseMakerPagination<ResponseEventDto>> getAllEvents(Pageable pageable) {
//        CourseMakerPagination<Event> eventPage = eventService.findAll(pageable);
//        CourseMakerPagination<ResponseEventDto> responseEventPage = eventPage.map(ResponseEventDto::toDto);
//        return ResponseEntity.ok(responseEventPage);
//    }
}
