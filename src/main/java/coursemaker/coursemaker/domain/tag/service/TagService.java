package coursemaker.coursemaker.domain.tag.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.dto.TagUpdateDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface TagService {
    TagResponseDto createTag(TagPostDto tag);

    TagResponseDto findById(Long id);

    TagResponseDto findByTagName(String name);

    List<TagResponseDto> findAllTags();

    TagResponseDto updateTag(TagUpdateDto tag);

    // ISSUE: 이것도 제대로 삭제됬는지 검증하는 절차가 필요할까요?
    void deleteById(Long id);

    /******태그-코스 ******/

    void addTagsByCourse(Long courseId, List<Long> tagIds);

    List<TagResponseDto> findAllByCourseId(Long courseId);

    CourseMakerPagination<TravelCourse> findAllCourseByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy);

    void deleteTagByCourse(Long courseId, List<TagResponseDto> tags);

    void deleteAllTagByCourse(Long courseId);



    /******태그-여행지 ******/

    void addTagsByDestination(Long destinationId, List<Long> tagIds);

    List<TagResponseDto> findAllByDestinationId(Long destinationId);

    // 특정 태그에 맞는 여행지 검색
    CourseMakerPagination<Destination> findAllDestinationByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy); ;


    void deleteTagByDestination(Long destinationId, List<TagResponseDto> tags);

    void deleteAllTagByDestination(Long destinationId);
}
