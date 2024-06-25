package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationDuplicatedException;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.exception.IllegalDestinationArgumentException;
import coursemaker.coursemaker.domain.destination.exception.PictureNotFoundException;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.OrderBy;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;
    private final TagService tagService;
    private final MemberService memberService;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository, @Lazy TagService tagService, MemberService memberService) {
        this.destinationRepository = destinationRepository;
        this.tagService = tagService;
        this.memberService = memberService;
    }

    @Override
    public Destination save(@Valid RequestDto requestDto) {
        // 멤버를 가져옴
        Member member = memberService.findByNickname(requestDto.getNickname());

        // 여행지 이름 중복 확인
        if (destinationRepository.existsByName(requestDto.getName())) {
            throw new DestinationDuplicatedException("여행지 이름이 이미 존재합니다.", "Destination name: " + requestDto.getName());
        }

        // DTO를 엔티티로 변환
        Destination destination = requestDto.toEntity(member);

        // 여행지 엔티티를 저장
        Destination savedDestination = destinationRepository.save(destination);

        // 태그를 추가
        tagService.addTagsByDestination(savedDestination.getId(), getTagIds(requestDto));

        // 여행지 엔티티를 저장
        return destinationRepository.save(destination);
    }

    @Override
    public Destination update(Long id, @Valid RequestDto requestDto) {

        // 기존 여행지 엔티티를 찾고 없으면 예외 처리
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지가 없습니다.", "Destination id: " + id));

        // 여행지 이름 중복 확인 (본인 제외)
        if (destinationRepository.existsByNameAndIdNot(requestDto.getName(), id)) {
            throw new DestinationDuplicatedException("여행지 이름이 이미 존재합니다.", "Destination name: " + requestDto.getName());
        }

        // 멤버 정보는 유지하고, 업데이트할 정보를 다시 설정
        destination.setName(requestDto.getName());
        destination.setPictureLink(requestDto.getPictureLink());
        destination.setContent(requestDto.getContent());
        destination.setLocation(requestDto.getLocation().getAddress());
        destination.setLongitude(requestDto.getLocation().getLongitude());
        destination.setLatitude(requestDto.getLocation().getLatitude());

        // 태그 삭제하고 다시 추가
        tagService.deleteAllTagByDestination(destination.getId());
        tagService.addTagsByDestination(destination.getId(), getTagIds(requestDto));

        return destinationRepository.save(destination);
    }

    // 통합 예외 처리

    // 1. RequestDto 객체에서 태그 ID를 추출합니다.
    // 2. 중복된 태그 ID를 제거합니다.
    // 3. 태그 ID를 List<Long> 형태로 반환합니다.
    private List<Long> getTagIds(RequestDto requestDto) {
        return requestDto.getTags().stream()
                .map(TagResponseDto::getId)
                .distinct()
                .toList();
    }
    @Override
    public Destination findById(Long id) {
        // ID로 여행지를 찾고, 없으면 예외처리
        return destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지가 없습니다.", "Destination id: " + id));
    }

    @Override
    public List<Destination> findAll() {
        // 저장된 모든 여행지를 리스트로 반환
        return destinationRepository.findAll();
    }

    @Override
    public CourseMakerPagination<Destination> findAll(Pageable pageable) {
        Page<Destination> page = destinationRepository.findAll(pageable);
        long total = tagService.findAllDestinationByTagIds(null,pageable,OrderBy.NEWEST).getTotalContents();
        CourseMakerPagination<Destination> courseMakerPagination = new CourseMakerPagination<>(pageable, page, total);
        return courseMakerPagination;
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
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지를 찾을수 없습니다: " + destinationId, "Destination id: " + destinationId));
        destination.setPictureLink(pictureLink);
        destinationRepository.save(destination);
    }

    // 여행지 id로 여행지의 대표사진 URL을 조회하는 메서드
    @Override
    public String getPictureLink(Long destinationId) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지를 찾을수 없습니다: " + destinationId, "Destination id: " + destinationId));
        String pictureLink = destination.getPictureLink();
        if (pictureLink.isEmpty()) {
            throw new PictureNotFoundException(ErrorCode.ILLEGAL_DESTINATION_ARGUMENT, "Destination id: " + destinationId);
        }
        return pictureLink;
    }

    // 기존 여행지의 대표사진 URL을 변경하는 메서드.
    @Override
    public void updatePictureLink(Long destinationId, String newPictureLink) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지를 찾을수 없습니다: " + destinationId, "Destination id: " + destinationId));
        destination.setPictureLink(newPictureLink);
        destinationRepository.save(destination);
    }

    // 특정 여행지의 대표사진 링크를 삭제하는 메서드.
    @Override
    public void deletePictureLink(Long destinationId) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지를 찾을수 없습니다: " + destinationId, "Destination id: " + destinationId));
        if (destination.getPictureLink().isEmpty()) {
            throw new PictureNotFoundException(ErrorCode.ILLEGAL_DESTINATION_ARGUMENT, "Destination id: " + destinationId);
        }
        // 대표사진 링크만 삭제
        destination.setPictureLink(null);
        destinationRepository.save(destination);
    }

    @Override
    public Destination getLocation(Long destinationId, LocationDto locationDto) {
        // 여행지 id를 이용해서 dto내용들 위치, 경도, 위도 설정
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new DestinationNotFoundException("해당하는 여행지를 찾을수 없습니다: " + destinationId, "Destination id: " + destinationId));

        destination.setLocation(locationDto.getAddress());
        destination.setLatitude(locationDto.getLatitude());
        destination.setLongitude(locationDto.getLongitude());
        return destinationRepository.save(destination);
    }
}
