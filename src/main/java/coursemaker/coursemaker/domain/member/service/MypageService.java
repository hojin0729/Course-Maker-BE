package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.dto.TravelCourseResponse;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.like.dto.CourseLikeResponseDto;
import coursemaker.coursemaker.domain.like.dto.DestinationLikeResponseDto;
import coursemaker.coursemaker.domain.like.service.CourseLikeService;
import coursemaker.coursemaker.domain.like.service.DestinationLikeService;
import coursemaker.coursemaker.domain.member.dto.BasicUserInfoResponseDTO;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.review.service.CourseReviewService;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.service.CourseWishService;
import coursemaker.coursemaker.domain.wish.service.DestinationWishService;
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
    private final TagService tagService;

    private final CourseService courseService;
    private final CourseDestinationService courseDestinationService;
    private final CourseLikeService courseLikeService;
    private final CourseWishService courseWishService;
    private final CourseReviewService courseReviewService;

    private final DestinationWishService destinationWishService;
    private final DestinationService destinationService;
    private final DestinationLikeService destinationLikeService;
    private final DestinationReviewService destinationReviewService;

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

        /*사용자 정보 입력*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<TravelCourseResponse> contents = courseToDTO(travelCoursePage.getContents(), loginedInfo);

        Page<TravelCourseResponse> responsePage = new PageImpl<>(contents, pageable, travelCoursePage.getTotalPage());

        return new CourseMakerPagination<>(pageable, responsePage, travelCoursePage.getTotalContents());
    }

    /*내가 찜한 코스 반환*/
    public CourseMakerPagination<TravelCourseResponse> getMyWishCourse(String nickname, Pageable pageable){

        /*위시리스트 받아옴*/
        List<CourseWishResponseDto> courseWishesByNickname = courseWishService.getCourseWishesByNickname(nickname);

        List<TravelCourse> courseWishList = new ArrayList<>();
        /*받아온 리스트를 코스 객체로 변환함*/
        for(CourseWishResponseDto wish : courseWishesByNickname){
            courseWishList.add(courseService.findById(wish.getCourseId()));
        }

        /*사용자 정보 입력*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<TravelCourseResponse> contents = courseToDTO(courseWishList, loginedInfo);

        return new CourseMakerPagination<>(pageable, contents);
    }

    /*내가 좋아요 한 코스 반환*/
    public CourseMakerPagination<TravelCourseResponse> getMyLikeCourse(String nickname, Pageable pageable){

        /*좋아요  받아옴*/
        List<CourseLikeResponseDto> courseLikesByNickname = courseLikeService.getCourseLikesByNickname(nickname);

        List<TravelCourse> courseLikeList = new ArrayList<>();
        /*받아온 리스트를 코스 객체로 변환함*/
        for(CourseLikeResponseDto wish : courseLikesByNickname){
            courseLikeList.add(courseService.findById(wish.getCourseId()));
        }

        /*사용자 정보 입력*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<TravelCourseResponse> contents = courseToDTO(courseLikeList, loginedInfo);

        return new CourseMakerPagination<>(pageable, contents);
    }

    /*내가 만든 여행지 반환*/
    public CourseMakerPagination<DestinationDto> getMyDestination(String nickname, Pageable pageable) {
        CourseMakerPagination<Destination> destinationPage = destinationService.findByMemberNickname(nickname, pageable);

        /*사용자 정보 입력*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<DestinationDto> contents = destinationToDTO(destinationPage.getContents(), loginedInfo);

        Page<DestinationDto> responsePage = new PageImpl<>(contents, pageable, destinationPage.getTotalPage());

        return new CourseMakerPagination<>(pageable, responsePage, destinationPage.getTotalContents());
    }

    /*내가 찜한 여행지 반환*/
    public CourseMakerPagination<DestinationDto> getMyWishDestination(String nickname, Pageable pageable){

        /*위시리스트 받아옴*/
        List<DestinationWishResponseDto> destinationWishesByNickname = destinationWishService.getDestinationWishesByNickname(nickname);

        List<Destination> destinationWishList = new ArrayList<>();
        /*받아온 리스트를 여행지 객체로 변환함*/
        for(DestinationWishResponseDto wish : destinationWishesByNickname){
            destinationWishList.add(destinationService.findById(wish.getDestinationId()));
        }

        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<DestinationDto> contents = destinationToDTO(destinationWishList, loginedInfo);

        return new CourseMakerPagination<>(pageable, contents);
    }

    /*내가 좋아요 한 여행지 반환*/
    public CourseMakerPagination<DestinationDto> getMyLikeDestination(String nickname, Pageable pageable){

        /*좋아요  받아옴*/
        List<DestinationLikeResponseDto> destinationLikesByNickname = destinationLikeService.getDestinationLikesByNickname(nickname);

        List<Destination> destinationLikeList = new ArrayList<>();
        /*받아온 리스트를 여행지 객체로 변환함*/
        for(DestinationLikeResponseDto like : destinationLikesByNickname){
            destinationLikeList.add(destinationService.findById(like.getDestinationId()));
        }

        /*사용자 정보 입력*/
        LoginedInfo loginedInfo = new LoginedInfo();
        loginedInfo.setNickname(nickname);

        List<DestinationDto> contents = destinationToDTO(destinationLikeList, loginedInfo);

        return new CourseMakerPagination<>(pageable, contents);
    }

    private List<TravelCourseResponse> courseToDTO(List<TravelCourse> courses, LoginedInfo loginedInfo) {
        List<TravelCourseResponse> dtos = new ArrayList<>();
        for (TravelCourse travelCourse : courses) {

            Boolean isMyWishCourse = courseWishService.isCourseWishedByUser(travelCourse.getId(), loginedInfo.getNickname());
            Boolean isMyLikeCourse = courseLikeService.isCourseLikedByUser(travelCourse.getId(), loginedInfo.getNickname());

            List<CourseDestinationResponse> courseDestinationResponses = courseDestinationService.getCourseDestinations(travelCourse)
                    .stream()
                    .map(courseDestination -> courseDestinationService.toResponse(courseDestination, loginedInfo))
                    .toList();

            List<TagResponseDto> tags = tagService.findAllByCourseId(travelCourse.getId());
            Double averageRating = courseReviewService.getAverageRating(travelCourse.getId());
            Integer reviewCount = courseReviewService.getReviewCount(travelCourse.getId());
            Integer wishCount = courseWishService.getCourseWishCount(travelCourse.getId());
            Integer likeCount = courseLikeService.getCourseLikeCount(travelCourse.getId());

            dtos.add(new TravelCourseResponse(travelCourse, courseDestinationResponses, tags, true, averageRating, reviewCount, wishCount, likeCount, isMyWishCourse, isMyLikeCourse));
        }

        return dtos;
    }

    /*destination 객체 => DTO*/
    private List<DestinationDto> destinationToDTO(List<Destination> destinations, LoginedInfo loginedInfo) {
        List<DestinationDto> dtos = destinations.stream()
                .map(destination -> {
                    Boolean isMyDestination = loginedInfo.getNickname().equals(destination.getMember().getNickname());
                    Boolean isMyWishDestination = destinationWishService.isDestinationWishedByUser(destination.getId(), loginedInfo.getNickname());
                    Boolean isMyLikeDestination = destinationLikeService.isDestinationLikedByUser(destination.getId(), loginedInfo.getNickname());
                    List<TagResponseDto> tags = tagService.findAllByDestinationId(destination.getId());
                    Double averageRating = destinationReviewService.getAverageRating(destination.getId());
                    Integer reviewCount = destinationReviewService.getReviewCount(destination.getId());
                    Integer wishCount = destinationWishService.getDestinationWishCount(destination.getId());
                    Integer likeCount = destinationLikeService.getDestinationLikeCount(destination.getId());
                    return DestinationDto.toDto(destination, tags, destination.getIsApiData(), averageRating, isMyDestination, reviewCount, wishCount, likeCount, isMyWishDestination, isMyLikeDestination);
                })
                .toList();

        return dtos;
    }

}
