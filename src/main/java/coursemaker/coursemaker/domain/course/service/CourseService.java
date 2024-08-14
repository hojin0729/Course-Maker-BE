package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    TravelCourse save(AddTravelCourseRequest request);
    CourseMakerPagination<TravelCourse> findAll(Pageable pageable);
    CourseMakerPagination<TravelCourse> getAllOrderByViewsDesc(Pageable pageable);
    TravelCourse findById(Long id);
    CourseMakerPagination<TravelCourse> findByTitleContaining(String title, Pageable pageable);
    // 닉네임으로 코스를 검색하는 메서드
    CourseMakerPagination<TravelCourse> findByMemberNickname(String nickname, Pageable pageable);
    TravelCourse update(Long id, UpdateTravelCourseRequest request, String nickname);
    void delete(Long id, String nickname);
    TravelCourse incrementViews(Long id);
    void addPictureLink(Long courseId, String pictureLink);

    // 코스 id로 여행지의 대표사진 URL을 조회하는 메서드
    String getPictureLink(Long courseId);

    // 기존 코스의 대표사진 URL을 새 URL로 변경하는 메서드
    void updatePictureLink(Long courseId, String newPictureLink);

    // 특정 코스의 대표사진을 삭제하는 메서드
    void deletePictureLink(Long courseId);

}