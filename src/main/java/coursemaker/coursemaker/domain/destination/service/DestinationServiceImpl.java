package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.entity.DestinationPicture;
import coursemaker.coursemaker.domain.destination.repository.DestinationPictureRepository;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;
    private final DestinationPictureRepository destinationPictureRepository;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository, DestinationPictureRepository destinationPictureRepository) {
        this.destinationRepository = destinationRepository;
        this.destinationPictureRepository = destinationPictureRepository;
    }

    @Override
    public Destination save(Destination destination) {
        // 여행지 엔티티를 저장
        return destinationRepository.save(destination);
    }

    @Override
    public Optional<Destination> findById(Long id) {
        // ID로 여행지를 찾아 Optional로 반환
        return destinationRepository.findById(id);
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

    @Override
    public DestinationPicture savePicture(DestinationPicture destinationPicture) {
        // 여행지 사진 엔티티를 저장
        return destinationPictureRepository.save(destinationPicture);
    }

    @Override
    public Optional<DestinationPicture> findPictureById(Long id) {
        //  ID 값으로 여행지 사진을 찾아서 반환
        return destinationPictureRepository.findById(id);
    }

    @Override
    public List<DestinationPicture> findAllPictures() {
        // 모든 여행지 사진 목록을 조회
        return destinationPictureRepository.findAll();
    }

    @Override
    public void deletePictureById(Long id) {
        // 주어진 ID의 여행지 사진을 삭제
        destinationPictureRepository.deleteById(id);
    }
}
