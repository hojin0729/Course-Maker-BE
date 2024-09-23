package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.exception.ForbiddenException;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.review.dto.RequestCourseDto;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.domain.review.entity.CourseReviewRecommendation;
import coursemaker.coursemaker.domain.review.exception.DuplicatedReviewException;
import coursemaker.coursemaker.domain.review.exception.ReviewNotFoundException;
import coursemaker.coursemaker.domain.review.repository.CourseReviewRecommendationRepository;
import coursemaker.coursemaker.domain.review.repository.CourseReviewRepository;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class CourseReviewServiceImpl implements CourseReviewService {
    private final CourseReviewRepository courseReviewRepository;
    private final CourseService courseService;
    private final TagService tagService;
    private final MemberService memberService;
    private final CourseReviewRecommendationRepository courseReviewRecommendationRepository;

    @Autowired
    public CourseReviewServiceImpl(CourseReviewRepository courseReviewRepository, CourseService courseService, TagService tagService, MemberService memberService, CourseReviewRecommendationRepository courseReviewRecommendationRepository) {
        this.courseReviewRepository = courseReviewRepository;
        this.courseService = courseService;
        this.tagService = tagService;
        this.memberService = memberService;
        this.courseReviewRecommendationRepository = courseReviewRecommendationRepository;
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

    public CourseReview update(Long reviewId, @Valid RequestCourseDto requestCourseDto, String nickname) {
        log.info("[CourseReview] 리뷰 업데이트 시작 - 리뷰 ID: {}, 닉네임: {}", reviewId, nickname);

        Member member = memberService.findByNickname(nickname);

        // 리뷰 ID를 기반으로 CourseReview 조회
        CourseReview existingReview = courseReviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("[CourseReview] 리뷰 찾기 실패 - 닉네임: {}, 리뷰 ID: {}", nickname, reviewId);
                    return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] reviewId: " + reviewId);
                });

        if (!existingReview.getMember().getNickname().equals(nickname)) {
            log.error("[CourseReview] 권한 없음 - 닉네임: {}, 리뷰 ID: {}", nickname, reviewId);
            throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.", "[CourseReview] reviewId: " + reviewId);
        }

        existingReview.setDescription(requestCourseDto.getDescription());
        existingReview.setPictures(requestCourseDto.getPictures());
        existingReview.setRating(requestCourseDto.getRating());

        CourseReview updatedReview = courseReviewRepository.save(existingReview);
        log.info("[CourseReview] 리뷰 업데이트 완료 - 리뷰 ID: {}, 닉네임: {}", updatedReview.getId(), nickname);

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
    public CourseMakerPagination<CourseReview> findAllByCourseId(Long courseId, Pageable pageable, OrderBy orderBy, String nickname) {
        log.info("[CourseReview] 코스 ID로 리뷰 목록 조회 시작 - 코스 ID: {}, 사용자: {}", courseId, nickname);

        Page<CourseReview> myReviews;
        Page<CourseReview> otherReviews;

        switch (orderBy) {
            case RATING_UP:
                myReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameOrderByRatingDesc(courseId, nickname, pageable); // 별점 높은 순
                otherReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameNotOrderByRatingDesc(courseId, nickname, pageable);
                break;
            case RATING_DOWN:
                myReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameOrderByRatingAsc(courseId, nickname, pageable); // 별점 낮은 순
                otherReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameNotOrderByRatingAsc(courseId, nickname, pageable);
                break;
            case NEWEST:
                myReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameOrderByCreatedAtDesc(courseId, nickname, pageable); // 최신순
                otherReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameNotOrderByCreatedAtDesc(courseId, nickname, pageable);
                break;
            case RECOMMEND:
                myReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameOrderByRecommendCountDesc(courseId, nickname, pageable); // 추천순
                otherReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameNotOrderByRecommendCountDesc(courseId, nickname, pageable);
                break;
            default:
                myReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNickname(courseId, nickname, pageable); // 기본 정렬
                otherReviews = courseReviewRepository.findAllByTravelCourseIdAndMemberNicknameNot(courseId, nickname, pageable);
                break;
        }

        List<CourseReview> allReviews = Stream.concat(myReviews.getContent().stream(), otherReviews.getContent().stream())
                        .toList();

        log.info("[CourseReview] 코스 ID로 리뷰 목록 조회 완료 - 코스 ID: {}, 총 리뷰 수: {}", courseId, allReviews.size());

        return new CourseMakerPagination<>(pageable, new PageImpl<>(allReviews, pageable, allReviews.size()), allReviews.size());
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

    @Override
    public void addRecommend(Long reviewId, String nickname) {
        Member member = memberService.findByNickname(nickname);
        CourseReview review = courseReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] reviewId: " + reviewId));

        // 이미 추천했는지 확인
        if (courseReviewRecommendationRepository.findByCourseReviewAndMember(review, member).isPresent()) {
            throw new IllegalStateException("이미 이 리뷰에 추천을 했습니다.");
        }

        // 추천 추가
        CourseReviewRecommendation recommendation = new CourseReviewRecommendation();
        recommendation.setCourseReview(review);
        recommendation.setMember(member);
        courseReviewRecommendationRepository.save(recommendation);

        review.setRecommendCount(review.getRecommendCount() + 1);
        courseReviewRepository.save(review);
    }

    @Override
    public void removeRecommend(Long reviewId, String nickname) {
        Member member = memberService.findByNickname(nickname);
        CourseReview review = courseReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] reviewId: " + reviewId));

        // 추천 기록을 찾고 삭제
        CourseReviewRecommendation recommendation = courseReviewRecommendationRepository.findByCourseReviewAndMember(review, member)
                .orElseThrow(() -> new IllegalStateException("추천한 적이 없습니다."));
        courseReviewRecommendationRepository.delete(recommendation);

        review.setRecommendCount(Math.max(0, review.getRecommendCount() - 1));
        courseReviewRepository.save(review);
    }

    @Override
    public boolean isReviewRecommendedByUser(Long reviewId, String nickname) {
        // 사용자의 정보를 가져옴
        Member member = memberService.findByNickname(nickname);

        // 리뷰 정보를 가져옴
        CourseReview review = courseReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[CourseReview] reviewId: " + reviewId));

        // 추천 여부 확인
        return courseReviewRecommendationRepository.findByCourseReviewAndMember(review, member).isPresent();
    }
}

