package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.tag.dto.TagDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;
    private final TagService tagService;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository, @Lazy TagService tagService) {
        this.destinationRepository = destinationRepository;
        this.tagService = tagService;
    }

    @Override
    public Destination save(Destination destination) {
        // 여행지 엔티티를 저장
        return destinationRepository.save(destination);
    }

    @Override
    public Destination findById(Long id) {
        // ID로 여행지를 찾고, 없으면 예외처리
        return destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("여행지가 없습니다."));
    }

    @Override
    public List<Destination> findAll() {
        // 저장된 모든 여행지를 리스트로 반환
        return destinationRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        // ID의 여행지를 삭제
        destinationRepository.deleteById(id);
    }

    // 여행지 id에 대한 대표사진을 추가하는 메서드
    @Override
    public void addPictureLink(Long destinationId, String pictureLink) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("여행지가 존재 하지 않습니다: " + destinationId));
        destination.setPictureLink(pictureLink);
        destinationRepository.save(destination);
    }

    // 여행지 id로 여행지의 대표사진 URL을 조회하는 메서드
    @Override
    public String getPictureLink(Long destinationId) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("여행지가 존재 하지 않습니다: " + destinationId));
        return destination.getPictureLink();
    }

    // 기존 여행지의 대표사진 URL을 변경하는 메서드.
    @Override
    public void updatePictureLink(Long destinationId, String newPictureLink) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("여행지가 존재하지 않습니다: " + destinationId));
        destination.setPictureLink(newPictureLink);
        destinationRepository.save(destination);
    }

    // 특정 여행지의 대표사진 링크를 삭제하는 메서드.
    @Override
    public void deletePictureLink(Long destinationId) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("여행지가 존재하지 않습니다: " + destinationId));
        // 대표사진 링크만 삭제
        destination.setPictureLink(null);
        destinationRepository.save(destination);
    }

    @Override
    public Destination getLocation(Long destinationId, LocationDto locationDto) {
        // 여행지 id를 이용해서 dto내용들 위치, 경도, 위도 찾기
        Optional<Destination> destinationLocation = destinationRepository.findById(destinationId);
        if (destinationLocation.isPresent()) {
            Destination destination = destinationLocation.get();
            destination.setLocation(locationDto.getLocation());
            destination.setLatitude(locationDto.getLatitude());
            destination.setLongitude(locationDto.getLongitude());
            return destinationRepository.save(destination);
        } else {
            throw new RuntimeException("해당하는 여행지를 찾을수 없습니다." + destinationId);
        }
    }

    @Override
    @Transactional
    public Destination saveDto(DestinationDto destinationDto) {
        // DTO를 엔티티로 변환
        Destination destination = DestinationDto.toEntity(destinationDto);

        // 엔티티를 저장
        Destination savedDestination = destinationRepository.save(destination);

        // 태그들을 저장
        tagService.AddTagsByDestination(
                savedDestination.getId(),
                destinationDto.getTags().stream()
                        .map(TagDto::toEntity)
                        .map(Tag::getId)
                        .collect(Collectors.toList())
        );

        return savedDestination;
    }

    // Id로 여행지를 조회하고, DTO로 변환하여 반환
    @Override
    public DestinationDto findDtoById(Long id) {
        // Id로 여행지를 조회
        Destination destination = findById(id);
        // 태그 목록을 조회
        List<TagDto> tagDtos = findTagsByDestinationId(id);
        // 엔티티를 DTO로 변환하여 반환
        return DestinationDto.toDto(destination, tagDtos);
    }

    // 저장된 모든 여행지를 DTO 리스트로 변환하여 반환
    @Override
    public List<DestinationDto> findAllDtos() {
        return destinationRepository.findAll().stream()
                .map(destination -> {
                    // 각 여행지의 태그 목록을 조회
                    List<TagDto> tagDtos = findTagsByDestinationId(destination.getId());
                    // 엔티티를 DTO로 변환
                    return DestinationDto.toDto(destination, tagDtos);
                })
                // 변환된 DTO 리스트를 반환
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> findTagsByDestinationId(Long destinationId) {
        // 여행지 ID로 태그 리스트를 조회
        List<Tag> tags = tagService.findAllByDestinationId(destinationId);
        return tags.stream().map(tag -> {
            TagDto dto = new TagDto();
            dto.setName(tag.getName());
            dto.setDescription(tag.getDescription());
            // 태그를 DTO로 변환
            return dto;
        }).collect(Collectors.toList()); // 변환된 DTO 리스트를 반환
    }
}
