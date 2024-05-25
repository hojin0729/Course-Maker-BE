package coursemaker.coursemaker.domain.tag.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.repository.CourseTagRepository;
import coursemaker.coursemaker.domain.tag.repository.DestinationTagRepository;
import coursemaker.coursemaker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private DestinationTagRepository destinationTagRepository;
    @Mock
    private DestinationService destinationService;

    // TODO: 코스 도메인 서비스레이어 개발시 적용
    @Mock
    private CourseTagRepository courseTagRepository;
    @Mock
    private CourseService courseService;

    @InjectMocks
    private TagServiceImpl tagService;

    /*스텁 데이터*/
    public List<Tag> CreateTags(){
        Tag tag = new Tag();
        List<Tag> tags = new ArrayList<>();

        for(Long i = 0L; i < 5L; i++){
            tag.setId(i);
            tag.setDescription("description" + i);
            tag.setName("tag" + i);
            tags.add(tag);
        }

        return tags;
    }

    public List<TravelCourse> createCourses(){
        TravelCourse course = TravelCourse.builder().build();
        List<TravelCourse> courses = new ArrayList<>();
        for(Long i = 0L; i < 5L; i++){
            course.setId(i);
            course.setTitle("title" + i);
            courses.add(course);
        }
        return courses;
    }

    public List<Destination> createDestinations(){
        Destination destination = new Destination();
        List<Destination> destinations = new ArrayList<>();
        for(Long i = 0L; i < 5L; i++){
            destination.setId(i);
            destination.setName("name" + i);
//            destination.setDescription("description" + i);
            destinations.add(destination);
        }
        return destinations;
    }


    @Test
    void createTag() {
        // given

    }

    @Test
    void createTag_중복생성() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByTagName() {
    }

    @Test
    void updateTag() {
    }

    @Test
    void deleteById() {
    }

    /*TODO: 코스 서비스 개발시 테스트코드 적용*/
//    @Test
//    void addTagsByCourse() {
//    }
//
//    @Test
//    void findAllByCourseId() {
//    }
//
//    @Test
//    void findAllCourseByTagId() {
//    }
//
//    @Test
//    void findCourseByTags() {
//    }
//
//    @Test
//    void deleteTagByCourse() {
//    }
//
//    @Test
//    void deleteAllTagByCourse() {
//    }

    @Test
    void addTagsByDestination() {
    }

    @Test
    void findAllByDestinationId() {
    }

    @Test
    void findAllDestinationByTagId() {
    }

    @Test
    void findDestinationByTags() {
    }

    @Test
    void deleteTagByDestination() {
    }

    @Test
    void deleteAllTagByDestination() {
    }
}