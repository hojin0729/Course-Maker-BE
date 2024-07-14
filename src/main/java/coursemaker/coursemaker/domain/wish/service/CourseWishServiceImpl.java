package coursemaker.coursemaker.domain.wish.service;


import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.repository.CourseWishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseWishServiceImpl implements CourseWishService {

    private CourseWishRepository courseWishRepository;
    private TravelCourseRepository travelCourseRepository;


    /* 코스 찜목록 전체조회*/
    @Override
    public List<CourseWish> getAllCourseWishes() {
        return courseWishRepository.findAll();
    }


    /* 코스 찜목록 아이디로 조회 */
    @Override
    public CourseWish getCourseWishById(Long id) {
        return courseWishRepository.findById(id).orElseThrow(()
                -> new RuntimeException("해당 코스 찜 정보가 없습니다."));
    }

    /* 코스 찜하기 */
    @Override
    public CourseWish addCourseWish(Long courseId) {
        TravelCourse travelCourse = travelCourseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("해당 여행코스를 찾을 수 없습니다."));

        CourseWish courseWish = courseWishRepository.findByTravelCourseId(courseId);
        if (courseWish != null) {
            // 기존에 존재하는 경우 카운트 증가
            courseWish.setCount(courseWish.getCount() + 1);
        } else {
            // 존재하지 않는 경우 새로운 CourseWish 생성
            courseWish = new CourseWish();
            courseWish.setTravelCourse(travelCourse);
            courseWish.setCount(1L); // 초기 카운트 설정
        }

        return courseWishRepository.save(courseWish);
    }

    /* 찜하기 취소 */
    @Override
    public void cancelCourseWish(Long courseId) {
        CourseWish courseWish = courseWishRepository.findByTravelCourseId(courseId);
        if (courseWish != null) {
            // 카운트 감소
            courseWish.setCount(courseWish.getCount() - 1);
            if (courseWish.getCount() <= 0) {
                // 카운트가 0 이하이면 삭제
                courseWishRepository.delete(courseWish);
            } else {
                // 그렇지 않으면 업데이트
                courseWishRepository.save(courseWish);
            }
        } else {
            throw new RuntimeException("해당 찜하기가 존재하지 않습니다.");
        }
    }
}
