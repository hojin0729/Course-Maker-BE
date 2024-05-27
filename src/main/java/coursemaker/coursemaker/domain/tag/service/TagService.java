package coursemaker.coursemaker.domain.tag.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);

    Tag findById(Long id);

    Tag findByTagName(String name);

    List<Tag> findAllTags();

    // ISSUE: 제대로 업데이트 됬는지 확인하고 싶은데 어떻게 해야 깔끔하게 할 수 있을까요? 아니면 굳이 검증을 해서 반환을 할 필요가 없을까요?
    Tag updateTag(Tag tag);

    // ISSUE: 이것도 제대로 삭제됬는지 검증하는 절차가 필요할까요?
    void deleteById(Long id);

    /******태그-코스 ******/
    /*TODO: 코스 도메인 엔티티, 서비스레이어 완성시 연결 및 검증*/

    // ISSUE: 코스에 맞는 태그들을 추가하는 메소드 입니다. 이때 어떤 반환값을 주는게 좋을까요?
    // 추가적으로, 태그가 제대로 추가됬는지 검증하는 로직이 필요할까요?
    void addTagsByCourse(Long courseId, List<Long> tagIds);

    List<Tag> findAllByCourseId(Long courseId);

    List<TravelCourse> findAllCourseByTagIds(List<Long> tagIds);

    void deleteTagByCourse(Long courseId, List<Tag> tags);

    void deleteAllTagByCourse(Long courseId);



    /******태그-여행지 ******/
    /*TODO: 여행지 도메인 엔티티, 서비스레이어 완성시 연결 및 검증*/

    void addTagsByDestination(Long destinationId, List<Long> tagIds);

    List<Tag> findAllByDestinationId(Long destinationId);

    // 특정 태그에 맞는 여행지 검색
    List<Destination> findAllDestinationByTagIds(List<Long> tagIds);


    void deleteTagByDestination(Long destinationId, List<Tag> tags);

    void deleteAllTagByDestination(Long destinationId);
}
