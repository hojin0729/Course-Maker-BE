package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.entity.DestinationPicture;

import java.util.List;
import java.util.Optional;

public interface DestinationService {
    // 여행지를 저장하는 메서드
    Destination save(Destination destination);


    // id 기반으로 특정 여행지를 조회하는 메서드
    Optional<Destination> findById(Long id);

    // 모든 여행지를 조회하는 메서드
    List<Destination> findAll();

    // id 기반으로 여행지를 삭제하는 메서드
    void deleteById(Long id);

    // 여행지 사진을 저장하는 메서드
    DestinationPicture savePicture(DestinationPicture destinationPicture);

    // id 기반으로 특정 여행지 사진을 조회하는 메서드
    Optional<DestinationPicture> findPictureById(Long id);

    // 모든 여행지 사진을 조회하는 메서드
    List<DestinationPicture> findAllPictures();

    // id 기반으로 여행지 사진을 삭제하는 메서드
    void deletePictureById(Long id);
}
