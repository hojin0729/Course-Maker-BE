package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DestinationServiceImplTest {
    @Mock
    private DestinationRepository destinationRepository;
    @InjectMocks
    private DestinationServiceImpl destinationService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        // given
        Destination destination = new Destination();
        when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

        // when
        Destination savedDestination = destinationService.save(destination);

        // then: 저장된 객체가 null이 아닌지, 저장 메소드가 적절히 호출되었는지 확인
        assertNotNull(savedDestination);
        verify(destinationRepository).save(destination);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void deleteById() {
    }
}