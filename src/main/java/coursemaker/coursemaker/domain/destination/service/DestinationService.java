package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.dto.TagDto;

import java.util.List;

public interface DestinationService {
    // 여행지를 저장하는 메서드
    Destination save(Destination destination);

    // id 기반으로 특정 여행지를 조회하는 메서드
    Destination findById(Long id);

    // 모든 여행지를 조회하는 메서드
    List<Destination> findAll();

    // id 기반으로 여행지를 삭제하는 메서드
    void deleteById(Long id);

    // 여행지 id에 대한 대표사진을 추가하는 메서드
    void addPictureLink(Long destinationId, String pictureLink);

    // 여행지 id로 여행지의 대표사진 URL을 조회하는 메서드
    String getPictureLink(Long destinationId);

    // 기존 여행지의 대표사진 URL을 새 URL로 변경하는 메서드
    void updatePictureLink(Long destinationId, String newPictureLink);

    // 특정 여행지의 대표사진을 삭제하는 메서드
    void deletePictureLink(Long destinationId);

    // 위치 정보 메서드
    Destination getLocation(Long destinationId, LocationDto locationDto);

    // DestinationDto를 받아 여행지를 저장하는 메서드
    Destination saveDto(DestinationDto destinationDto);

    // Id를 기반으로 여행지를 조회하고, DestinationDto로 변환하여 반환하는 메서드
    DestinationDto findDtoById(Long id);

    // 모든 여행지를 조회하고, DestinationDto 리스트로 변환하여 반환하는 메서드
    List<DestinationDto> findAllDtos();

    // 여행지 Id로 태그 리스트를 조회하는 메서드
    List<TagDto> findTagsByDestinationId(Long destinationId);

}
