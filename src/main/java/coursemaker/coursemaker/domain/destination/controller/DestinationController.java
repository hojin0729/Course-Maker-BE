package coursemaker.coursemaker.domain.destination.controller;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
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
    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinations = destinationService.findAll();
        return destinations.stream()
                .map(destination -> {
                    // Destination의 ID로 관련된 모든 태그를 가져옴.
                    List<TagResponseDto> tags = tagService.findAllByDestinationId(destination.getId())
                            .stream()
                            // 각 태그 엔티티를 TagResponseDto로 변환하고 리스트로 수집.
                            .map(Tag::toResponseDto)
                            .toList();
                    // Destination 엔티티와 태그 리스트를 이용하여 DestinationDto로 변환
                    return DestinationDto.toDto(destination, tags);
                })
                .toList();
    }

    // id에 해당하는 여행지의 상세 정보를 가져옴.
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable("id") Long id) {
        Destination destination = destinationService.findById(id);
        List<TagResponseDto> tags = tagService.findAllByDestinationId(id)
                .stream()
                .map(Tag::toResponseDto)
                .toList();
        DestinationDto destinationDto = DestinationDto.toDto(destination, tags);
        return ResponseEntity.ok(destinationDto);
    }

    // 여행지를 새로 생성함.
    @PostMapping
    public ResponseEntity<Void> createDestination(@RequestBody DestinationDto request) {
        Destination destination = DestinationDto.toEntity(request);
        Destination savedDestination = destinationService.save(destination);

        if (!request.getTags().isEmpty()) {
            List<Long> tagIds = request.getTags()
                    .stream()
                    .map(TagResponseDto::getId)
                    .toList();
            for (Long tagId : tagIds) {
                System.out.println("tagId = " + tagId);
            }
            tagService.addTagsByDestination(savedDestination.getId(), tagIds);
        }

        return ResponseEntity.created(URI.create("/v1/destination/" + savedDestination.getId())).build();
    }
    // Id에 해당하는 여행지의 정보를 삭제합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteDestinationById(@PathVariable("id") Long id) {
        // 해당 ID의 여행지가 존재하는지 확인합니다.
        Destination destination = destinationService.findById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        // 여행지에 연결된 태그들을 먼저 삭제합니다.
        tagService.deleteAllTagByDestination(id);
        // 여행지 자체를 삭제합니다.
        destinationService.deleteById(id);
        return ResponseEntity.ok(id);
    }
}
