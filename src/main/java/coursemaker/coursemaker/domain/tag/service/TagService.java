package coursemaker.coursemaker.domain.tag.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);

    Tag findById(Long id);

    Tag findByTagName(String name);

    List<Tag> findAllTags();

    Tag updateTag(Tag tag);

    // ISSUE: 이것도 제대로 삭제됬는지 검증하는 절차가 필요할까요?
    void deleteById(Long id);

    /******태그-코스 ******/

    void addTagsByCourse(Long courseId, List<Long> tagIds);

    List<Tag> findAllByCourseId(Long courseId);

    CourseMakerPagination<TravelCourse> findAllCourseByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy);

    void deleteTagByCourse(Long courseId, List<Tag> tags);

    void deleteAllTagByCourse(Long courseId);



    /******태그-여행지 ******/

    void addTagsByDestination(Long destinationId, List<Long> tagIds);

    List<Tag> findAllByDestinationId(Long destinationId);

    // 특정 태그에 맞는 여행지 검색
    CourseMakerPagination<Destination> findAllDestinationByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy); ;


    void deleteTagByDestination(Long destinationId, List<Tag> tags);

    void deleteAllTagByDestination(Long destinationId);
}
