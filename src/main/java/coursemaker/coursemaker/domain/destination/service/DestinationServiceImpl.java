package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
