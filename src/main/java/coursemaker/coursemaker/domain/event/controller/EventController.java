package coursemaker.coursemaker.domain.event.controller;

import coursemaker.coursemaker.domain.event.dto.RequestEventDto;
import coursemaker.coursemaker.domain.event.dto.ResponseEventDto;
import coursemaker.coursemaker.domain.event.entity.Event;
import coursemaker.coursemaker.domain.event.service.EventService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("v1/events")
@Tag(name = "Event", description = "이벤트 관리 API")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "이벤트 ID로 이벤트 조회", description = "이벤트 ID를 사용하여 특정 이벤트의 상세 정보를 조회합니다.")
    @Parameter(name = "id", description = "조회할 이벤트의 ID", required = true, example = "1")
    public ResponseEntity<ResponseEventDto> getEventById(@PathVariable("id") Long id) {
        Event event = eventService.findById(id);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(event);
        return ResponseEntity.ok(responseEventDto);
    }

    @PostMapping
    @Operation(summary = "새 이벤트 생성", description = "새로운 이벤트 정보를 입력하여 이벤트를 생성합니다.")
    public ResponseEntity<ResponseEventDto> createEvent(@RequestBody @Valid RequestEventDto requestEventDto) {
        Event savedEvent = eventService.save(requestEventDto);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(savedEvent);
        return ResponseEntity.created(URI.create("/v1/events/" + savedEvent.getId())).body(responseEventDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "이벤트 업데이트", description = "이벤트 ID와 수정할 정보를 입력하여 해당 이벤트의 정보를 수정합니다.")
    @Parameter(name = "id", description = "수정할 이벤트의 ID", required = true, example = "1")
    public ResponseEntity<ResponseEventDto> updateEvent(@PathVariable("id") Long id,
                                                        @RequestBody @Valid RequestEventDto requestEventDto) {
        Event updatedEvent = eventService.update(id, requestEventDto);
        ResponseEventDto responseEventDto = ResponseEventDto.toDto(updatedEvent);
        return ResponseEntity.ok(responseEventDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이벤트 삭제", description = "이벤트 ID를 입력하여 해당 이벤트를 삭제합니다.")
    @Parameter(name = "id", description = "삭제할 이벤트의 ID", required = true, example = "1")
    public ResponseEntity<Long> deleteEvent(@PathVariable("id") Long id) {
        eventService.delete(id);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "이벤트 목록 페이지네이션 조회", description = "페이지네이션을 사용하여 이벤트 목록을 조회합니다.")
    @Parameter(name = "record", description = "페이지당 표시할 데이터 수", example = "20")
    @Parameter(name = "page", description = "조회할 페이지 번호 (페이지는 1부터 시작합니다.)", example = "1")
    @GetMapping
    public ResponseEntity<CourseMakerPagination<ResponseEventDto>> getAllEvents(
            @RequestParam(defaultValue = "20", name = "record") int record,
            @RequestParam(defaultValue = "1", name = "page") int page) {

        Pageable pageable = PageRequest.of(page - 1, record);

        CourseMakerPagination<Event> eventPage = eventService.findAll(pageable);
        List<Event> eventList = eventPage.getContents();

        List<ResponseEventDto> responseDtos = eventList.stream()
                .map(ResponseEventDto::toDto)
                .collect(Collectors.toList());

        CourseMakerPagination<ResponseEventDto> responseEventPage = new CourseMakerPagination<>(
                pageable,
                new PageImpl<>(responseDtos, pageable, eventPage.getTotalContents()),
                eventPage.getTotalContents()
        );

        return ResponseEntity.ok(responseEventPage);
    }
}
