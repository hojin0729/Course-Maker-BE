package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    TravelCourse save(AddTravelCourseRequest request);
    CourseMakerPagination<TravelCourse> findAll(Pageable pageable);
    CourseMakerPagination<TravelCourse> getAllOrderByViewsDesc(Pageable pageable);
    TravelCourse findById(Long id);
    TravelCourse update(Long id, AddTravelCourseRequest request);
    void delete(Long id);
    TravelCourse incrementViews(Long id);
    void addPictureLink(Long courseId, String pictureLink);

    // 코스 id로 여행지의 대표사진 URL을 조회하는 메서드
    String getPictureLink(Long courseId);

    // 기존 코스의 대표사진 URL을 새 URL로 변경하는 메서드
    void updatePictureLink(Long courseId, String newPictureLink);

    // 특정 코스의 대표사진을 삭제하는 메서드
    void deletePictureLink(Long courseId);

}
