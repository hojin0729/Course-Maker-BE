package coursemaker.coursemaker.domain.tag.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import coursemaker.coursemaker.domain.course.entity.QTravelCourse;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.entity.CourseTag;
import coursemaker.coursemaker.domain.tag.entity.QCourseTag;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.repository.CourseTagRepository;
import coursemaker.coursemaker.domain.tag.repository.DestinationTagRepository;
import coursemaker.coursemaker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private DestinationTagRepository destinationTagRepository;
    @Mock
    private DestinationService destinationService;

    @Mock
    JPAQueryFactory queryFactory;

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
    @DisplayName("일반 태그 생성")
    void createTag() {
        // given
        Tag tag = new Tag();
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        // when
        Tag actual = tagService.createTag(tag);

        // then
        assertNotNull(actual);
        verify(tagRepository).save(tag);

    }

    @Test
    @DisplayName("태그 중복 생성")
    void createTag_중복생성() {
        // given
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");
        tag.setDescription("description1");

        // when
        when(tagRepository.findByname(tag.getName())).thenReturn(Optional.of(tag));

        // then
        assertThrows(RuntimeException.class, () -> tagService.createTag(tag));
        verify(tagRepository).findByname(tag.getName());
    }

    @Test
    @DisplayName("태그 검색")
    void findById() {
        // given
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");
        tag.setDescription("description1");
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        // when
        Tag actual = tagService.findById(1L);

        // then
        assertEquals(tag, actual);
        verify(tagRepository).findById(1L);
    }

    @Test
    @DisplayName("태그 이름으로 검색")
    void findByTagName() {
        // given
        Tag tag = new Tag();
        tag.setName("tag1");
        tag.setDescription("description1");

        when(tagRepository.findByname(tag.getName())).thenReturn(Optional.of(tag));

        // when
        Tag actual = tagService.findByTagName(tag.getName());

        // then
        assertEquals(tag, actual);
        verify(tagRepository).findByname(tag.getName());
    }

    @Test
    @DisplayName("태그 이름이 없을때 검색결과")
    void findByTagName_이름X() {
        // given
        Tag tag = new Tag();
        tag.setName("tag1");
        tag.setDescription("description1");

        when(tagRepository.findByname(tag.getName())).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(RuntimeException.class, () -> tagService.findByTagName(tag.getName()) );
        verify(tagRepository).findByname(tag.getName());
    }

    @Test
    @DisplayName("태그 정보 수정")
    void updateTag() {
        // given
        Tag tag = new Tag();
        tag.setName("업데이트 태그");
        tag.setDescription("description1");

        when(tagRepository.findByname(tag.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));

        // when
        Tag actual = tagService.updateTag(tag);

        // then
        assertEquals(tag, actual);

        verify(tagRepository).findByname(tag.getName());
        verify(tagRepository).save(tag);
        verify(tagRepository).findById(tag.getId());
    }

    // ISSUE: 이거 어케 검증함? 걍 리포지토리에서 삭제하는게 끝임...
    @Test
    void deleteById() {
    }

    @Test
    @DisplayName("코스에 태그 추가 - 성공")
    void addTagsByCourse() {
        // given
        Long courseId = 1L;
        Long tagId = 2L;
        List<Long> tagIds = Arrays.asList(tagId);
        TravelCourse course = TravelCourse.builder().build();
        Tag tag = new Tag();

        // when
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        when(courseService.findById(courseId)).thenReturn(course);
        when(courseTagRepository.findByCourseIdAndTagId(courseId, tagId)).thenReturn(Optional.empty());

        tagService.addTagsByCourse(courseId, tagIds);

        // then
        verify(tagRepository).findById(tagId);
        verify(courseService).findById(courseId);
        verify(courseTagRepository).findByCourseIdAndTagId(courseId, tagId);
        verify(courseTagRepository).save(any(CourseTag.class));
    }

    @Test
    @DisplayName("해당 코스에 있는 모든 태그 검색")
    void findAllByCourseId() {
        // given
        Long courseId = 1L;
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        CourseTag courseTag1 = new CourseTag();
        courseTag1.setTag(tag1);

        CourseTag courseTag2 = new CourseTag();
        courseTag2.setTag(tag2);
        List<CourseTag> courseTags = Arrays.asList(courseTag1, courseTag2);

        // when
        when(courseTagRepository.findAllByCourseId(courseId)).thenReturn(courseTags);
        List<Tag> foundTags = tagService.findAllByCourseId(courseId);

        // then
        assertNotNull(foundTags);
        assertEquals(2, foundTags.size());
        assertTrue(foundTags.containsAll(Arrays.asList(tag1, tag2)));
    }

    // TODO: query dsl 테스트코드 작성
    @Test
    void findAllCourseByTagIds() {
    }

    @Test
    @DisplayName("코스에서 특정 태그 삭제 ")
    void deleteTagByCourse() {
        // given
        Long courseId = 1L;
        Tag tag1 = new Tag();
        tag1.setId(2L);
        Tag tag2 = new Tag();
        tag2.setId(3L);

        List<Tag> tags = Arrays.asList(tag1, tag2);

        // when
        when(courseTagRepository.findByCourseIdAndTagId(courseId, tag1.getId())).thenReturn(Optional.of(new CourseTag()));
        when(courseTagRepository.findByCourseIdAndTagId(courseId, tag2.getId())).thenReturn(Optional.of(new CourseTag()));

        tagService.deleteTagByCourse(courseId, tags);

        // then
        verify(courseTagRepository).deleteByCourseIdAndTagId(courseId, tag1.getId());
        verify(courseTagRepository).deleteByCourseIdAndTagId(courseId, tag2.getId());
    }

    @Test
    @DisplayName("코스에서 모든 태그 삭제")
    void deleteAllTagByCourse() {
        Long courseId = 1L;

        tagService.deleteAllTagByCourse(courseId);

        verify(courseTagRepository).deleteAllByCourseId(courseId);
    }

    /*TODO: 여행지-태그 테스트코드 작성!*/
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