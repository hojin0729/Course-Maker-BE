package coursemaker.coursemaker.domain.destination.service;

import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationDuplicatedException;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DestinationServiceImplTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationServiceImpl destinationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* 스텁 데이터 생성 메서드 */
    public List<Destination> createDestinations() {
        Destination destination1 = new Destination();
        destination1.setId(1L);
        destination1.setName("Destination1");
        destination1.setPictureLink("http://example.com/pic1.jpg");
        destination1.setViews(100);
        destination1.setContent("Content1");
        destination1.setLocation("Location1");
        destination1.setLongitude(BigDecimal.valueOf(126.9780));
        destination1.setLatitude(BigDecimal.valueOf(37.5665));

        Destination destination2 = new Destination();
        destination2.setId(2L);
        destination2.setName("Destination2");
        destination2.setPictureLink("http://example.com/pic2.jpg");
        destination2.setViews(200);
        destination2.setContent("Content2");
        destination2.setLocation("Location2");
        destination2.setLongitude(BigDecimal.valueOf(126.9780));
        destination2.setLatitude(BigDecimal.valueOf(37.5665));

        return List.of(destination1, destination2);
    }

    @Test
    void save() {
        // Given
        Destination destination = new Destination();
        when(destinationRepository.existsById(destination.getId())).thenReturn(false);
        when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

        // When
        Destination savedDestination = destinationService.save(destination);

        // Then
        assertNotNull(savedDestination);
        verify(destinationRepository).save(destination);
    }

    @Test
    void save_DestinationExists_ThrowsException() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        when(destinationRepository.existsById(destination.getId())).thenReturn(true);

        // When & Then
        assertThrows(DestinationDuplicatedException.class, () -> destinationService.save(destination));
        verify(destinationRepository, never()).save(any(Destination.class));
    }

    @Test
    void findById() {
        // Given
        List<Destination> destinations = createDestinations();
        Destination destination = destinations.get(0);
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        // When
        Destination foundDestination = destinationService.findById(1L);

        // Then
        assertNotNull(foundDestination);
        assertEquals(destination, foundDestination);
    }

    @Test
    void findById_DestinationDoesNotExist_ThrowsException() {
        // Given
        when(destinationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DestinationNotFoundException.class, () -> destinationService.findById(1L));
    }

    @Test
    void findAll() {
        // Given
        List<Destination> destinations = createDestinations();
        when(destinationRepository.findAll()).thenReturn(destinations);

        // When
        List<Destination> foundDestinations = destinationService.findAll();

        // Then
        assertNotNull(foundDestinations);
        assertEquals(destinations, foundDestinations);
    }

    @Test
    void findAllPageable() {
        // Given
        List<Destination> destinations = createDestinations();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Destination> page = new PageImpl<>(destinations);
        when(destinationRepository.findAll(pageRequest)).thenReturn(page);

        // When
        Page<Destination> foundPage = destinationService.findAll(pageRequest);

        // Then
        assertNotNull(foundPage);
        assertEquals(page, foundPage);
    }

    @Test
    void deleteById() {
        // When
        destinationService.deleteById(1L);

        // Then
        verify(destinationRepository).deleteById(1L);
    }

    @Test
    void addPictureLink() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        // When
        destinationService.addPictureLink(1L, "http://example.com/picture.jpg");

        // Then
        assertEquals("http://example.com/picture.jpg", destination.getPictureLink());
        verify(destinationRepository).save(destination);
    }

    @Test
    void getPictureLink() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        destination.setPictureLink("http://example.com/picture.jpg");
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        // When
        String pictureLink = destinationService.getPictureLink(1L);

        // Then
        assertNotNull(pictureLink);
        assertEquals("http://example.com/picture.jpg", pictureLink);
    }

    @Test
    void updatePictureLink() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        // When
        destinationService.updatePictureLink(1L, "http://example.com/newpicture.jpg");

        // Then
        assertEquals("http://example.com/newpicture.jpg", destination.getPictureLink());
        verify(destinationRepository).save(destination);
    }

    @Test
    void deletePictureLink() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        destination.setPictureLink("http://example.com/picture.jpg");
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        // When
        destinationService.deletePictureLink(1L);

        // Then
        assertNull(destination.getPictureLink());
        verify(destinationRepository).save(destination);
    }

    @Test
    void getLocation() {
        // Given
        Destination destination = new Destination();
        destination.setId(1L);
        LocationDto locationDto = new LocationDto();
        locationDto.setLocation("부산광역시");
        locationDto.setLatitude(BigDecimal.valueOf(37.5665));
        locationDto.setLongitude(BigDecimal.valueOf(126.9780));
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
        when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

        // When
        Destination updatedDestination = destinationService.getLocation(1L, locationDto);

        // Then
        assertEquals("부산광역시", updatedDestination.getLocation());
        assertEquals(BigDecimal.valueOf(37.5665), updatedDestination.getLatitude());
        assertEquals(BigDecimal.valueOf(126.9780), updatedDestination.getLongitude());
        verify(destinationRepository).save(destination);
    }
}
