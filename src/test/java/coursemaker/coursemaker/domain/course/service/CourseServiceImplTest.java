package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
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
    private TravelCourseRepository travelCourseRepository;

    @Mock
    private CourseDestinationRepository courseDestinationRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveTravelCourse() {
        AddTravelCourseRequest request = new AddTravelCourseRequest(
                "Test Course",
                "This is a test course.",
                5,
                2,
                1,
                "http://example.com/picture.jpg",
                new ArrayList<>()
        );

        when(travelCourseRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(request.toEntity());

        TravelCourse savedCourse = courseService.save(request);

        assertNotNull(savedCourse);
        assertEquals(request.getTitle(), savedCourse.getTitle());
        verify(travelCourseRepository, times(1)).findByTitle(anyString());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }

    @Test
    void save_shouldThrowExceptionWhenDuplicateCourse() {
        AddTravelCourseRequest request = new AddTravelCourseRequest(
                "Test Course",
                "This is a test course.",
                5,
                2,
                1,
                "http://example.com/picture.jpg",
                new ArrayList<>()
        );

        when(travelCourseRepository.findByTitle(anyString())).thenReturn(Optional.of(request.toEntity()));

        assertThrows(TravelCourseDuplicatedException.class, () -> courseService.save(request));
        verify(travelCourseRepository, times(1)).findByTitle(anyString());
        verify(travelCourseRepository, never()).save(any(TravelCourse.class));
    }

    @Test
    void findAll_shouldReturnAllTravelCourses() {
        List<TravelCourse> travelCourses = new ArrayList<>();
        travelCourses.add(new TravelCourse("Test Course 1", "Content 1", 5, 2, 1, "http://example.com/picture1.jpg"));
        travelCourses.add(new TravelCourse("Test Course 2", "Content 2", 5, 2, 1, "http://example.com/picture2.jpg"));

        when(travelCourseRepository.findAll()).thenReturn(travelCourses);

        List<TravelCourse> result = courseService.findAll();

        assertEquals(2, result.size());
        verify(travelCourseRepository, times(1)).findAll();
    }

    @Test
    void getAllOrderByViewsDesc_shouldReturnOrderedTravelCourses() {
        List<TravelCourse> travelCourses = new ArrayList<>();
        TravelCourse course1 = new TravelCourse("Test Course 1", "Content 1", 5, 2, 1, "http://example.com/picture1.jpg");
        TravelCourse course2 = new TravelCourse("Test Course 2", "Content 2", 5, 2, 1, "http://example.com/picture2.jpg");
        course1.setViews(10);
        course2.setViews(20);
        travelCourses.add(course1);
        travelCourses.add(course2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TravelCourse> page = new PageImpl<>(travelCourses, pageable, travelCourses.size());

        when(travelCourseRepository.findAllByOrderByViewsDesc(pageable)).thenReturn(page);

        Page<TravelCourse> result = courseService.getAllOrderByViewsDesc(pageable);

        assertEquals(2, result.getTotalElements());
        verify(travelCourseRepository, times(1)).findAllByOrderByViewsDesc(pageable);
    }

    @Test
    void findById_shouldReturnTravelCourse() {
        TravelCourse travelCourse = new TravelCourse("Test Course", "Content", 5, 2, 1, "http://example.com/picture.jpg");

        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));

        TravelCourse result = courseService.findById(1L);

        assertNotNull(result);
        assertEquals(travelCourse.getTitle(), result.getTitle());
        verify(travelCourseRepository, times(1)).findById(anyLong());
    }

    @Test
    void findById_shouldThrowExceptionWhenCourseNotFound() {
        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TravelCourseNotFoundException.class, () -> courseService.findById(1L));
        verify(travelCourseRepository, times(1)).findById(anyLong());
    }

    @Test
    void update_shouldUpdateTravelCourse() {
        TravelCourse travelCourse = new TravelCourse("Old Title", "Old Content", 5, 2, 1, "http://example.com/old_picture.jpg");
        UpdateTravelCourseRequest request = new UpdateTravelCourseRequest(
                "New Title",
                "Updated content",
                10,
                3,
                2,
                "http://example.com/new_picture.jpg",
                new ArrayList<>()
        );

        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(travelCourse);

        TravelCourse updatedCourse = courseService.update(1L, request);

        assertEquals(request.getTitle(), updatedCourse.getTitle());
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }

    @Test
    void update_shouldThrowExceptionWhenCourseNotFound() {
        UpdateTravelCourseRequest request = new UpdateTravelCourseRequest(
                "New Title",
                "Updated content",
                10,
                3,
                2,
                "http://example.com/new_picture.jpg",
                new ArrayList<>()
        );

        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TravelCourseNotFoundException.class, () -> courseService.update(1L, request));
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, never()).save(any(TravelCourse.class));
    }

    @Test
    void delete_shouldDeleteTravelCourse() {
        when(travelCourseRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(travelCourseRepository).deleteById(anyLong());

        courseService.delete(1L);

        verify(travelCourseRepository, times(1)).existsById(anyLong());
        verify(travelCourseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void delete_shouldThrowExceptionWhenCourseNotFound() {
        when(travelCourseRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(TravelCourseNotFoundException.class, () -> courseService.delete(1L));
        verify(travelCourseRepository, times(1)).existsById(anyLong());
        verify(travelCourseRepository, never()).deleteById(anyLong());
    }

    @Test
    void incrementViews_shouldIncrementViews() {
        TravelCourse travelCourse = new TravelCourse("Test Course", "Content", 5, 2, 1, "http://example.com/picture.jpg");
        travelCourse.setViews(5);

        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.of(travelCourse));
        when(travelCourseRepository.save(any(TravelCourse.class))).thenReturn(travelCourse);

        TravelCourse updatedCourse = courseService.incrementViews(1L);

        assertEquals(6, updatedCourse.getViews());
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, times(1)).save(any(TravelCourse.class));
    }

    @Test
    void incrementViews_shouldThrowExceptionWhenCourseNotFound() {
        when(travelCourseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TravelCourseNotFoundException.class, () -> courseService.incrementViews(1L));
        verify(travelCourseRepository, times(1)).findById(anyLong());
        verify(travelCourseRepository, never()).save(any(TravelCourse.class));
    }
}