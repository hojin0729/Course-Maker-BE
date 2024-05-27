package coursemaker.coursemaker.domain.destination.controller;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("v1/destination")
public class DestinationController {
    private final DestinationService destinationService;
    private final TagService tagService;

    public DestinationController(DestinationService destinationService, TagService tagService) {
        this.destinationService = destinationService;
        this.tagService = tagService;
    }

    // 전체 여행지 목록을 가져옵니다.
    @GetMapping
    public List<Destination> getAllDestinations() {

        return destinationService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createDestination(@RequestBody DestinationDto request) {
        Destination destination = DestinationDto.toEntity(request);
        Destination savedDestination = destinationService.save(destination);

        return ResponseEntity.created(URI.create("/v1/destination/" + savedDestination.getId())).build();
    }
}
