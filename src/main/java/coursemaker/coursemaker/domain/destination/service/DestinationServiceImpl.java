package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationDuplicatedException;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.exception.PictureNotFoundException;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public Destination save(Destination destination) {
        // 여행지 엔티티를 저장
        return destinationRepository.save(destination);
    }

    @Override
    public Destination update(Destination destination) {
        // 여행지 엔티티를 업데이트
        if (!destinationRepository.existsById(destination.getId())) {
            throw new DestinationNotFoundException("해당하는 여행지가 없습니다.", "Destination id: " + destination.getId());
        }
        return destinationRepository.save(destination);
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
    public Page<Destination> findAll(Pageable pageable) {
        return destinationRepository.findAll(pageable);
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
            throw new PictureNotFoundException("사진이 존재하지 않습니다.", "Destination id: " + destinationId);
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
            throw new PictureNotFoundException("사진이 존재하지 않습니다.", "Destination id: " + destinationId);
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

        destination.setLocation(locationDto.getLocation());
        destination.setLatitude(locationDto.getLatitude());
        destination.setLongitude(locationDto.getLongitude());
        return destinationRepository.save(destination);
    }
}
