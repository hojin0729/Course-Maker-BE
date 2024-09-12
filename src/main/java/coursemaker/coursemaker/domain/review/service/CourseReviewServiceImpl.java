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
import org.springframework.dao.DataIntegrityViolationException;
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
        log.info("[CourseReview] 리뷰 저장 시작 - 코스 ID: {}", courseId);
        Member member = memberService.findByNickname(requestCourseDto.getNickname());
        TravelCourse travelCourse = courseService.findById(courseId);

        CourseReview courseReview = requestCourseDto.toEntity(member);
        courseReview.setTravelCourse(travelCourse);

        try {
            CourseReview savedReview = courseReviewRepository.save(courseReview);
            log.info("[CourseReview] 리뷰 저장 완료 - 리뷰 ID: {}, 코스 ID: {}", savedReview.getId(), courseId);
            return savedReview;
        } catch (DataIntegrityViolationException e) {
            log.error("[CourseReview] 중복 리뷰 오류 - 닉네임: {}, 코스 ID: {}", courseReview.getMember().getNickname(), courseId);
            throw new DuplicatedReviewException("해당 코스에 이미 리뷰를 남겼습니다.",
                    "[CourseReview] nickname: " + courseReview.getMember().getNickname() +
                            ", course id: " + courseReview.getTravelCourse().getId());
        }
    }

    @Override
    public CourseReview update(Long courseId, @Valid RequestCourseDto requestCourseDto, String nickname) {
        log.info("[CourseReview] 리뷰 업데이트 시작 - 코스 ID: {}, 닉네임: {}", courseId, nickname);

        Member member = memberService.findByNickname(nickname);
        TravelCourse travelCourse = courseService.findById(courseId);

        CourseReview existingReview = courseReviewRepository.findByMemberAndTravelCourse(member, travelCourse)
                .orElseThrow(() -> {
                    log.error("[CourseReview] 리뷰 찾기 실패 - 닉네임: {}, 코스 ID: {}", nickname, courseId);
                    return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] nickname: " + nickname);
                });

        existingReview.setTitle(requestCourseDto.getTitle());
        existingReview.setDescription(requestCourseDto.getDescription());
        existingReview.setPicture(requestCourseDto.getPicture());
        existingReview.setRating(requestCourseDto.getRating());

        CourseReview updatedReview = courseReviewRepository.save(existingReview);
        log.info("[CourseReview] 리뷰 업데이트 완료 - 리뷰 ID: {}, 코스 ID: {}", updatedReview.getId(), courseId);

        return updatedReview;
    }

    @Override
    public void delete(Long id) {
        log.info("[CourseReview] 리뷰 삭제 시작 - 리뷰 ID: {}", id);
        courseReviewRepository.findById(id).orElseThrow(() -> {
            log.error("[CourseReview] 리뷰 찾기 실패 - 리뷰 ID: {}", id);
            return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] delete id: " + id);
        });
        courseReviewRepository.deleteById(id);
        log.info("[CourseReview] 리뷰 삭제 완료 - 리뷰 ID: {}", id);
    }

    @Override
    public CourseReview findById(Long id) {
        log.info("[CourseReview] 리뷰 조회 시작 - 리뷰 ID: {}", id);
        return courseReviewRepository.findById(id)
                .orElseThrow(() -> {
                    return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] id: " + id);
                });
    }

    @Override
    public CourseMakerPagination<CourseReview> findAllByCourseId(Long courseId, Pageable pageable) {
        log.info("[CourseReview] 코스 ID로 리뷰 목록 조회 시작 - 코스 ID: {}", courseId);
        Page<CourseReview> page = courseReviewRepository.findByTravelCourseId(courseId, pageable);
        log.info("[CourseReview] 코스 ID로 리뷰 목록 조회 완료 - 코스 ID: {}, 총 리뷰 수: {}", courseId, page.getTotalElements());
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }

    @Override
    public Double getAverageRating(Long courseId) {
        log.info("[CourseReview] 코스 평균 평점 계산 시작 - 코스 ID: {}", courseId);
        List<CourseReview> reviews = courseReviewRepository.findByTravelCourseId(courseId);

        log.debug("Debug: 리뷰 수 = {}", reviews.size());

        for (CourseReview review : reviews) {
            log.debug("Debug: 리뷰 ID = {}, 평점 = {}", review.getId(), review.getRating());
        }

        // 평균 평점 계산
        double averageRating = reviews.stream().mapToDouble(CourseReview::getRating).average().orElse(0.0);

        // 평균 평점을 소수점 첫째 자리로 포맷팅
        DecimalFormat df = new DecimalFormat("#.#");
        double formattedAverageRating = Double.parseDouble(df.format(averageRating));

        log.info("[CourseReview] 코스 평균 평점 계산 완료 - 코스 ID: {}, 평균 평점: {}", courseId, formattedAverageRating);

        return formattedAverageRating;
    }

    @Override
    public Integer getReviewCount(Long courseId) {
        log.info("[CourseReview] 코스 리뷰 개수 조회 시작 - 코스 ID: {}", courseId);
        int reviewCount = courseReviewRepository.countByTravelCourseId(courseId);
        log.info("[CourseReview] 코스 리뷰 개수 조회 완료 - 코스 ID: {}, 리뷰 개수: {}", courseId, reviewCount);
        return reviewCount;
    }

    @Override
    public CourseMakerPagination<CourseReview> findByMemberNickname(String nickname, Pageable pageable) {
        log.info("[CourseReview] 멤버 닉네임으로 코스 리뷰 조회 - 닉네임: {}", nickname);
        Page<CourseReview> page = courseReviewRepository.findByMemberNicknameAndDeletedAtIsNull(nickname, pageable);
        long total = page.getTotalElements();
        return new CourseMakerPagination<>(pageable, page, total);
    }
}

