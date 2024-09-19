package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.like.service.CourseLikeService;
import coursemaker.coursemaker.domain.member.dto.BasicUserInfoResponseDTO;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.review.service.CourseReviewService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final MemberService memberService;
    private final CourseService courseService;
    private final CourseDestinationService courseDestinationService;
    private final TagService tagService;
    private final DestinationService destinationService;
    private final CourseLikeService courseLikeService;
    private final CourseWishService courseWishService;
    private final CourseReviewService courseReviewService;

    /**
     * 도메인 분리 이유: 순환참조
     * 코스 -> 멤버 참조(사용자 정보 필요),
     * 멤버 -> 코스 참조(코스 정보 필요)
     * => 순환참조 발생으로 서브도메인으로 분리함.
     * TODO: 마이페이지 기능 구현
     * */

    /*사용자 기본정보 반환*/
    public BasicUserInfoResponseDTO getBasicUserInfo(String nickname) {

        Member member = memberService.findByNickname(nickname);

        BasicUserInfoResponseDTO response = new BasicUserInfoResponseDTO();

        response.setNickname(member.getNickname());
        response.setName(member.getName());
        response.setRole(Role.toKor(member.getRoles().getRole()));

        System.out.println(member.getRoles().getRole());

        return response;
    }

    /*내가 만든 코스 반환*/
    public CourseMakerPagination<TravelCourseResponse> getMyCourses(String nickname, Pageable pageable) {
        CourseMakerPagination<TravelCourse> travelCoursePage = courseService.findByMemberNickname(nickname, pageable);

        /*더미 객체 생성*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<TravelCourseResponse> contents = new ArrayList<>();
        for (TravelCourse travelCourse : travelCoursePage.getContents()) {

            Boolean isMyWishCourse = courseWishService.isCourseWishedByUser(travelCourse.getId(), nickname);
            Boolean isMyLikeCourse = courseLikeService.isCourseLikedByUser(travelCourse.getId(), nickname);

            List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(travelCourse)
                    .stream()
                    .map(courseDestination -> courseDestinationService.toResponse(courseDestination, loginedInfo))
                    .toList();

            List<TagResponseDto> tags = tagService.findAllByCourseId(travelCourse.getId());
            Double averageRating = courseReviewService.getAverageRating(travelCourse.getId());
            Integer reviewCount = courseReviewService.getReviewCount(travelCourse.getId());
            Integer wishCount = courseWishService.getCourseWishCount(travelCourse.getId());
            Integer likeCount = courseLikeService.getCourseLikeCount(travelCourse.getId());

            contents.add(new TravelCourseResponse(travelCourse, courseDestinationResponses, tags, true, averageRating, reviewCount, wishCount, likeCount, isMyWishCourse, isMyLikeCourse));
        }

        Page<TravelCourseResponse> responsePage = new PageImpl<>(contents, pageable, travelCoursePage.getTotalPage());

        return new CourseMakerPagination<>(pageable, responsePage, travelCoursePage.getTotalContents());
    }

    /*내가 찜한 코스 반환*/
    public CourseMakerPagination<TravelCourseResponse> getMyWishCourse(String nickname, Pageable pageable){

        /*위시리스트 받아옴*/
        List<CourseWishResponseDto> courseWishesByNickname = courseWishService.getCourseWishesByNickname(nickname);

        List<TravelCourse> wishCourses = new ArrayList<>();
        /*받아온 리스트를 코스 객체로 변환함*/
        for(CourseWishResponseDto wish : courseWishesByNickname){
            wishCourses.add(courseService.findById(wish.getCourseId()));
        }

        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<TravelCourseResponse> contents = new ArrayList<>();
        for (TravelCourse travelCourse : wishCourses) {

            Boolean isMyWishCourse = courseWishService.isCourseWishedByUser(travelCourse.getId(), nickname);
            Boolean isMyLikeCourse = courseLikeService.isCourseLikedByUser(travelCourse.getId(), nickname);

            List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(travelCourse)
                    .stream()
                    .map(courseDestination -> courseDestinationService.toResponse(courseDestination, loginedInfo))
                    .toList();

            List<TagResponseDto> tags = tagService.findAllByCourseId(travelCourse.getId());
            Double averageRating = courseReviewService.getAverageRating(travelCourse.getId());
            Integer reviewCount = courseReviewService.getReviewCount(travelCourse.getId());
            Integer wishCount = courseWishService.getCourseWishCount(travelCourse.getId());
            Integer likeCount = courseLikeService.getCourseLikeCount(travelCourse.getId());

            contents.add(new TravelCourseResponse(travelCourse, courseDestinationResponses, tags, true, averageRating, reviewCount, wishCount, likeCount, isMyWishCourse, isMyLikeCourse));
        }

        return new CourseMakerPagination<>(pageable, contents);
    }

}
