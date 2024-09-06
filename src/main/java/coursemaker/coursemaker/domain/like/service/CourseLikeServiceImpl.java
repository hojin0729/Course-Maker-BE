package coursemaker.coursemaker.domain.like.service;


import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.like.dto.CourseLikeRequestDto;
import coursemaker.coursemaker.domain.like.dto.CourseLikeResponseDto;
import coursemaker.coursemaker.domain.like.entity.CourseLike;
import coursemaker.coursemaker.domain.like.exception.CourseLikeNotFoundException;
import coursemaker.coursemaker.domain.like.exception.DuplicateLikeException;
import coursemaker.coursemaker.domain.like.repository.CourseLikeRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseLikeServiceImpl implements CourseLikeService {

    private CourseLikeRepository courseLikeRepository;
    private TravelCourseRepository travelCourseRepository;
    private MemberRepository memberRepository;

    public CourseLikeServiceImpl(CourseLikeRepository courseLikeRepository,
                                 TravelCourseRepository travelCourseRepository,
                                 MemberRepository memberRepository) {
        this.courseLikeRepository = courseLikeRepository;
        this.travelCourseRepository = travelCourseRepository;
        this.memberRepository = memberRepository;
    }


    /* 코스 좋아요목록 전체조회 */
    @Override
    public List<CourseLikeResponseDto> getAllCourseLikes() {

        List<CourseLike> likes = courseLikeRepository.findAll();

        if (likes.isEmpty()) {
            throw new CourseLikeNotFoundException("코스 좋아요가 존재하지 않습니다.", "Invalid like");
        }

        return likes.stream()
                .map(courseLike -> new CourseLikeResponseDto(
                        courseLike.getTravelCourse().getId(),
                        courseLike.getTravelCourse().getTitle(),
                        courseLike.getMember().getNickname()))
                .collect(Collectors.toList());
    }



    /* 코스 좋아요목록 닉네임으로 조회 */
    @Override
    public List<CourseLikeResponseDto> getCourseLikesByNickname(String nickname) {
        List<CourseLike> courseLikes = courseLikeRepository.findByMemberNickname(nickname);
        if (courseLikes.isEmpty()) {
            throw new CourseLikeNotFoundException("코스 좋아요가 존재하지 않습니다.", "Nickname: " + nickname);
        }
        return courseLikes.stream()
                .map(courseLike -> new CourseLikeResponseDto(
                        courseLike.getTravelCourse().getId(),
                        courseLike.getTravelCourse().getTitle(),
                        courseLike.getMember().getNickname()))
                .collect(Collectors.toList());
    }

    /* 코스 좋아요하기 */
    @Override
    @Transactional
    public CourseLikeResponseDto addCourseLike(CourseLikeRequestDto requestDto) {
        //dto로 로그인한 유저의 nickname 및 courseId 들어온다

        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(requestDto.getCourseId())
                .orElseThrow(() -> new TravelCourseNotFoundException("해당 코스를 찾을 수 없습니다.", "CourseId: " + requestDto.getCourseId()));

        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new UserNotFoundException("해당 멤버를 찾을 수 없습니다.", "Nickname: " + requestDto.getNickname()));

        // 중복 체크 로직 제거 (데이터베이스 유니크 제약 조건에 의해 처리됨)
//        boolean exists = courseLikeRepository.existsByTravelCourseIdAndMemberId(travelCourse.getId(), member.getId());
//        if (exists) {
//            throw new DuplicateLikeException("이미 이 코스를 좋아요했습니다.", "CourseId: " + travelCourse.getId() + ", Nickname: " + member.getNickname());
//        }

        CourseLike courseLike = new CourseLike();
        courseLike.setTravelCourse(travelCourse);
        courseLike.setMember(member);

        try {
            CourseLike savedLike = courseLikeRepository.save(courseLike);
            return new CourseLikeResponseDto(
                    savedLike.getTravelCourse().getId(),
                    savedLike.getTravelCourse().getTitle(),
                    savedLike.getMember().getNickname());
        } catch (DataIntegrityViolationException e) {
            // 유니크 제약 조건 위반 시 사용자 정의 예외 던지기
            throw new DuplicateLikeException("이미 이 코스를 좋아요했습니다.", "CourseId: " + travelCourse.getId() + ", Nickname: " + member.getNickname());
        }
    }

    /* 좋아요하기 취소 */
    @Override
    @Transactional
    public void cancelCourseLike(Long courseId, String nickname) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + nickname));

        // 좋아요 정보 가져오기
        CourseLike courseLike = courseLikeRepository.findByTravelCourseIdAndMemberId(courseId, member.getId())

                .orElseThrow(() -> new CourseLikeNotFoundException("해당 코스 좋아요가 존재하지 않습니다.", "CourseId: " + courseId + ", Nickname: " + nickname));


        // 코스 좋아요 삭제
        courseLikeRepository.delete(courseLike);
    }

    /* 특정 코스에 대한 좋아요 목록 조회 */
    @Override
    public List<CourseLikeResponseDto> getLikesByCourseId(Long courseId) {
        List<CourseLike> courseLikes = courseLikeRepository.findByTravelCourseId(courseId);
        if (courseLikes.isEmpty()) {
            throw new CourseLikeNotFoundException("해당 코스에 대한 좋아요가 존재하지 않습니다.", "CourseId: " + courseId);
        }
        return courseLikes.stream()
                .map(courseLike -> new CourseLikeResponseDto(
                        courseLike.getTravelCourse().getId(),
                        courseLike.getTravelCourse().getTitle(),
                        courseLike.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 코스별 좋아요된 수 조회 */
    @Override
    public Integer getCourseLikeCount(Long courseId) {
        // 코스가 존재하는지 확인
        travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당 코스를 찾을 수 없습니다.", "CourseId: " + courseId));

        // 코스에 대한 좋아요 수 반환
        return courseLikeRepository.countByTravelCourseId(courseId);
    }

    @Override
    public Boolean isCourseLikedByUser(Long courseId, String nickname) {
        // 코스가 존재하는지 확인
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당 여행지를 찾을 수 없습니다.", "CourseId: " + courseId));

        // 사용자가 존재하는지 확인
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + nickname));

        // 사용자가 해당 코스를 찜했는지 여부를 반환
        return courseLikeRepository.existsByTravelCourseIdAndMemberId(travelCourse.getId(), member.getId());
    }
}
