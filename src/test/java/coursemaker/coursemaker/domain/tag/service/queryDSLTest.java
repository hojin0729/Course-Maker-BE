package coursemaker.coursemaker.domain.tag.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.tag.entity.CourseTag;
import coursemaker.coursemaker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import coursemaker.coursemaker.domain.tag.entity.Tag;

import static coursemaker.coursemaker.domain.course.entity.QTravelCourse.travelCourse;
import static coursemaker.coursemaker.domain.tag.entity.QCourseTag.courseTag;
import static coursemaker.coursemaker.domain.tag.entity.QTag.tag;

@SpringBootTest
public class queryDSLTest {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TravelCourseRepository travelCourseRepository;


    @BeforeEach
    void setUp() {
        Tag tag = new Tag();
        List<Tag> tags = new ArrayList<>();

        for(int i = 1; i <= 5; i++) {
            Tag myTag = new Tag();
            myTag.setName("tag" + i);
            myTag.setDescription("test"+ i);
            myTag = tagService.createTag(myTag);
            tags.add(myTag);
        }

        TravelCourse travelCourse = TravelCourse.builder().build();
        List<TravelCourse> travelCourses = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            TravelCourse myCourse = TravelCourse.builder().build();
            myCourse.setTitle("course" + i);
            myCourse.setContent("test");
            myCourse.setDuration(1);
            myCourse.setTravelerCount(1);
            myCourse.setTravelType(1);
            myCourse=travelCourseRepository.save(myCourse);
            travelCourses.add(myCourse);
        }

        for(int i = 0; i < 3; i++) {
            travelCourse = travelCourses.get(i);
            tag = tags.get(i);
            tagService.addTagsByCourse(travelCourse.getId(), List.of(tag.getId()));
        }

        /*4번 코스 -> 1번 태그
        * 4번 코스 -> 2번 태그
        *====>
        * 1번 태그: 1번코스, 4번코스
        * 2번 태그: 2번코스, 4번코스
        * 1번, 2번 태그를 갖는 코스: 4번 코스*/
        travelCourse = travelCourses.get(3);
        tag = tags.get(0);
        tagService.addTagsByCourse(travelCourse.getId(), List.of(tag.getId()));

        travelCourse = travelCourses.get(3);
        tag = tags.get(1);
        tagService.addTagsByCourse(travelCourse.getId(), List.of(tag.getId()));

    }

    /* 4번 코스 -> 1번 태그
     * 4번 코스 -> 2번 태그
     *====>
     * 1번 태그: 1번코스, 4번코스
     * 2번 태그: 2번코스, 4번코스
     * 1번, 2번 태그를 갖는 코스: 4번 코스*/
    @Test
    void contextLoads() {

        /*다중태그 조건*/
        List<Long> searchList = new ArrayList<>();
        searchList.add(1L);
        searchList.add(2L);

        BooleanBuilder condition = new BooleanBuilder();
        /*다중검색*/
        for(Long id : searchList) {
            condition.or(courseTag.tag.id.eq(id));
        }

        Set<TravelCourse> duplicate = new HashSet<>();// group by로 묶어서 count 때려서 하면 됨
        System.out.println("-----------query----------");
//        List<TravelCourse> course2 = jpaQueryFactory
//                .select(courseTag)// 코스 형태로 반환
//                .from(courseTag)// 코스태그에서 선택(코스에는 FK가 없음)
//                .leftJoin(courseTag.course, travelCourse)// 코스-코스태그 조인
//                .where(condition)// 다중태그
//                .groupBy(courseTag)
////                .having(courseTag.course.count().gt(1))
//                .fetch()
//                .stream()
//                .map((n) -> n.getCourse())
////                .filter(n-> !duplicate.add(n))
//                .collect(Collectors.toList());


        List<TravelCourse> course2 = jpaQueryFactory
                .select(courseTag, courseTag.course.count())// 코스 형태로 반환
                .from(courseTag)// 코스태그에서 선택(코스에는 FK가 없음)
                .leftJoin(courseTag.course, travelCourse)// 코스-코스태그 조인
                .where(condition)// 다중태그
                .groupBy(courseTag.course)
                .having(courseTag.course.count().gt(searchList.size()-1))
                .fetch()
                .stream()
                .map(n -> n.get(courseTag).getCourse())
                .collect(Collectors.toList());

        System.out.println("-----------query----------");
        
        // 검색 조건에 맞는 코스 이름을 출력
        for(TravelCourse travelCourse : course2) {
            System.out.println("===============================================================travelCourse.getTitle() = " + travelCourse.getTitle() );
        }
    }
}
