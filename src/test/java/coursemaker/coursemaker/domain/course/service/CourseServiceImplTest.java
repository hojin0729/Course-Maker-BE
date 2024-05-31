package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseDestinationRepository courseDestinationRepository;

    @Mock
    private TravelCourseRepository travelCourseRepository;

    @Mock
    private CourseDestinationService courseDestinationService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        AddTravelCourseRequest request = new AddTravelCourseRequest(
                "Test Title",
                "Test Content",
                5,
                3,
                1,
                "test.jpg",
                new ArrayList<>()
        );

        TravelCourse travelCourse = request.toEntity();
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(travelCourse);

        TravelCourse savedCourse = courseService.save(request);

        assertNotNull(savedCourse);
        assertEquals("Test Title", savedCourse.getTitle());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }

    @Test
    void testFindAll() {
        List<TravelCourse> travelCourses = new ArrayList<>();
        travelCourses.add(new TravelCourse("title", "content", 5, 3, 1, "pictureLink"));
        when(travelCourseRepository.findAll()).thenReturn(travelCourses);

        List<TravelCourse> result = courseService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(travelCourseRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrderByViewsDesc() {
        List<TravelCourse> travelCourses = new ArrayList<>();
        travelCourses.add(new TravelCourse("title", "content", 5, 3, 1, "pictureLink"));
        Page<TravelCourse> page = new PageImpl<>(travelCourses);
        Pageable pageable = PageRequest.of(0, 10);
        when(travelCourseRepository.findAllByOrderByViewsDesc(pageable)).thenReturn(page);

        Page<TravelCourse> result = courseService.getAllOrderByViewsDesc(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(travelCourseRepository, times(1)).findAllByOrderByViewsDesc(pageable);
    }

    @Test
    void testFindById() {
        TravelCourse travelCourse = new TravelCourse("title", "content", 5, 3, 1, "pictureLink");
        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));

        TravelCourse result = courseService.findById(1L);

        assertNotNull(result);
        verify(travelCourseRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdate() {
        TravelCourse travelCourse = new TravelCourse("title", "content", 5, 3, 1, "pictureLink");
        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(travelCourse);

        UpdateTravelCourseRequest request = new UpdateTravelCourseRequest(
                "Updated Title",
                "Updated Content",
                10,
                5,
                2,
                "updated.jpg",
                new ArrayList<>()
        );

        TravelCourse updatedCourse = courseService.update(1L, request);

        assertNotNull(updatedCourse);
        assertEquals("Updated Title", updatedCourse.getTitle());
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }

    @Test
    void testDelete() {
        doNothing().when(travelCourseRepository).deleteById(anyLong());

        courseService.delete(1L);

        verify(travelCourseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testIncrementViews() {
        TravelCourse travelCourse = new TravelCourse("title", "content", 5, 3, 1, "pictureLink");
        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(travelCourse);

        TravelCourse result = courseService.incrementViews(1L);

        assertNotNull(result);
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }
}