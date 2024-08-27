package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.review.dto.RequestCourseDto;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.domain.review.exception.DuplicatedReviewException;
import coursemaker.coursemaker.domain.review.exception.ReviewNotFoundException;
import coursemaker.coursemaker.domain.review.repository.CourseReviewRepository;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@Service
public class CourseReviewServiceImpl implements CourseReviewService {
    private final CourseReviewRepository courseReviewRepository;
    private final CourseService courseService;
    private final TagService tagService;
    private final MemberService memberService;

    @Autowired
    public CourseReviewServiceImpl(CourseReviewRepository courseReviewRepository, CourseService courseService, TagService tagService, MemberService memberService) {
        this.courseReviewRepository = courseReviewRepository;
        this.courseService = courseService;
        this.tagService = tagService;
        this.memberService = memberService;
    }

    @Override
    public CourseReview save(@Valid RequestCourseDto requestCourseDto, Long courseId) {
        Member member = memberService.findByNickname(requestCourseDto.getNickname());
        TravelCourse travelCourse = courseService.findById(courseId);
        if (courseReviewRepository.findByMemberAndTravelCourse(member, travelCourse).isPresent()) {
            throw new DuplicatedReviewException("해당 코스에 이미 리뷰를 남겼습니다.",
                    "[CourseReview] nickname: "+ member.getNickname() + " course id: " + courseId);
        }
        CourseReview courseReview = requestCourseDto.toEntity(member);
        courseReview.setTravelCourse(travelCourse);
        return courseReviewRepository.save(courseReview);
    }

    @Override
    public CourseReview update(Long courseId, @Valid RequestCourseDto requestCourseDto, String nickname) {
        Member member = memberService.findByNickname(nickname);
        TravelCourse travelCourse = courseService.findById(courseId);

        CourseReview existingReview = courseReviewRepository.findByMemberAndTravelCourse(member, travelCourse)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] nickname: " + nickname));

        existingReview.setTitle(requestCourseDto.getTitle());
        existingReview.setDescription(requestCourseDto.getDescription());
        existingReview.setPicture(requestCourseDto.getPicture());
        existingReview.setRating(requestCourseDto.getRating());

        return courseReviewRepository.save(existingReview);
    }

    @Override
    public void delete(Long id) {
        courseReviewRepository.findById(id).orElseThrow(() ->
                new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] delete id: "+ id)
        );
        courseReviewRepository.deleteById(id);
    }

    @Override
    public CourseReview findById(Long id) {
        return courseReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] id: "+ id));
    }

    @Override
    public CourseMakerPagination<CourseReview> findAllByCourseId(Long courseId, Pageable pageable) {
        Page<CourseReview> page = courseReviewRepository.findByTravelCourseId(courseId, pageable);
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }

    @Override
    public Double getAverageRating(Long courseId) {
        List<CourseReview> reviews = courseReviewRepository.findByTravelCourseId(courseId);

        log.debug("Debug: Number of reviews found = " + reviews.size());

        for (CourseReview review : reviews) {
            log.debug("Debug: Review ID = " + review.getId() + ", Rating = " + review.getRating());
        }

        // 평균 평점 계산
        double averageRating = reviews.stream().mapToDouble(CourseReview::getRating).average().orElse(0.0);

        // 평균 평점을 소수점 첫째 자리로 포맷팅
        DecimalFormat df = new DecimalFormat("#.#");
        double formattedAverageRating = Double.parseDouble(df.format(averageRating));

        log.debug("Debug: Calculated formatted averageRating = " + formattedAverageRating);

        return formattedAverageRating;
    }
}

