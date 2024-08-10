package coursemaker.coursemaker.domain.wish.service;


import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.repository.CourseWishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseWishServiceImpl implements CourseWishService {

    private CourseWishRepository courseWishRepository;
    private TravelCourseRepository travelCourseRepository;
    private MemberRepository memberRepository;


    /* 코스 찜목록 전체조회*/
    @Override
    public List<CourseWish> getAllCourseWishes() {
        return courseWishRepository.findAll();
    }


    /* 코스 찜목록 닉네임으로 조회 */
    @Override
    public List<CourseWish> getCourseWishesByNickname(String nickname) {
        List<CourseWish> courseWishes = courseWishRepository.findByMemberNickname(nickname);
        if (courseWishes.isEmpty()) {
            throw new RuntimeException("해당 코스 찜 정보가 없습니다.");
        }
        return courseWishes;
    }

    /* 코스 찜하기 */
    @Override
    public CourseWish addCourseWish(Long courseId, Long memberId) {
        TravelCourse travelCourse = travelCourseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("해당 코스를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));

        CourseWish courseWish = new CourseWish();
        courseWish.setTravelCourse(travelCourse);
        courseWish.setMember(member);

        return courseWishRepository.save(courseWish);
    }

    /* 찜하기 취소 */
    @Override
    public void cancelCourseWish(Long courseId, Long memberId) {
        Optional<CourseWish> optionalCourseWish = courseWishRepository.findByTravelCourseIdAndMemberId(courseId, memberId);
        if (optionalCourseWish.isPresent()) {
            courseWishRepository.delete(optionalCourseWish.get());
        } else {
            throw new RuntimeException("해당 찜하기가 존재하지 않습니다.");
        }
    }
}
