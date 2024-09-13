package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.exception.DuplicatedReviewException;
import coursemaker.coursemaker.domain.review.exception.ReviewNotFoundException;
import coursemaker.coursemaker.domain.review.repository.DestinationReviewRepository;

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
public class DestinationReviewServiceImpl implements DestinationReviewService {
    private final DestinationReviewRepository destinationReviewRepository;
    private final DestinationService destinationService;
    private final MemberService memberService;

    @Autowired
    public DestinationReviewServiceImpl(DestinationReviewRepository destinationReviewRepository, DestinationService destinationService, MemberService memberService) {
        this.destinationReviewRepository = destinationReviewRepository;
        this.destinationService = destinationService;
        this.memberService = memberService;
    }

    @Override
    public DestinationReview save(@Valid RequestDestinationDto requestDestinationDto, Long destinationId) {
        log.info("[DestinationReview] 리뷰 저장 시작 - 여행지 ID: {}", destinationId);
        Member member = memberService.findByNickname(requestDestinationDto.getNickname());
        Destination destination = destinationService.findById(destinationId);

        // 중복 체크 로직 제거 (데이터베이스 유니크 제약 조건에 의해 처리됨)
//        if (destinationReviewRepository.findByMemberAndDestination(member, destination).isPresent()) {
//            throw new DuplicatedReviewException("해당 여행지에 이미 리뷰를 남겼습니다.",
//                    "[DestinationReview] nickname: "+ member.getNickname() + " destination id: " + destinationId);
//        }
        DestinationReview destinationReview = requestDestinationDto.toEntity(member);
        destinationReview.setDestination(destination);

        try {
            log.debug("Debug: 저장 전 리뷰 평점 = {}", destinationReview.getRating());
            DestinationReview savedReview = destinationReviewRepository.save(destinationReview);
            log.info("[DestinationReview] 리뷰 저장 완료 - 리뷰 ID: {}, 여행지 ID: {}", savedReview.getId(), destinationId);
            return savedReview;
        } catch (DataIntegrityViolationException e) {
            log.error("[DestinationReview] 중복 리뷰 오류 - 닉네임: {}, 여행지 ID: {}", member.getNickname(), destinationId);
            throw new DuplicatedReviewException("해당 여행지에 이미 리뷰를 남겼습니다.",
                    "[DestinationReview] nickname: " + member.getNickname() + ", destination id: " + destinationId);
        }
    }


    @Override
    public DestinationReview update(Long destinationId, @Valid RequestDestinationDto requestDestinationDto, String nickname) {
        log.info("[DestinationReview] 리뷰 업데이트 시작 - 여행지 ID: {}, 닉네임: {}", destinationId, nickname);

        Member member = memberService.findByNickname(nickname);
        Destination destination = destinationService.findById(destinationId);

        DestinationReview existingReview = destinationReviewRepository.findByMemberAndDestination(member, destination)
                .orElseThrow(() -> {
                    log.error("[DestinationReview] 리뷰 찾기 실패 - 닉네임: {}, 여행지 ID: {}", nickname, destinationId);
                    return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] nickname: " + nickname);
                });

        existingReview.setTitle(requestDestinationDto.getTitle());
        existingReview.setDescription(requestDestinationDto.getDescription());
        existingReview.setPictures(requestDestinationDto.getPictures());
        existingReview.setRating(requestDestinationDto.getRating());

        DestinationReview updatedReview = destinationReviewRepository.save(existingReview);
        log.info("[DestinationReview] 리뷰 업데이트 완료 - 리뷰 ID: {}, 여행지 ID: {}", updatedReview.getId(), destinationId);

        return updatedReview;
    }

    @Override
    public void delete(Long id) {
        log.info("[DestinationReview] 리뷰 삭제 시작 - 리뷰 ID: {}", id);
        destinationReviewRepository.findById(id).orElseThrow(() -> {
            log.error("[DestinationReview] 리뷰 찾기 실패 - 리뷰 ID: {}", id);
            return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] delete id: " + id);
        });
        destinationReviewRepository.deleteById(id);
        log.info("[DestinationReview] 리뷰 삭제 완료 - 리뷰 ID: {}", id);
    }

    @Override
    public DestinationReview findById(Long id) {
        log.info("[DestinationReview] 리뷰 조회 시작 - 리뷰 ID: {}", id);
        return destinationReviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[DestinationReview] 리뷰 찾기 실패 - 리뷰 ID: {}", id);
                    return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] id: " + id);
                });
    }


    @Override
    public CourseMakerPagination<DestinationReview> findAllByDestinationId(Long destinationId, Pageable pageable, OrderBy orderBy) {
        log.info("[DestinationReview] 여행지 ID로 리뷰 목록 조회 시작 - 여행지 ID: {}", destinationId);

        Page<DestinationReview> reviews;
        switch (orderBy) {
            case RATING_UP:
                reviews = destinationReviewRepository.findAllByDestinationIdOrderByRatingDesc(destinationId, pageable); // 별점 높은 순
                break;
            case RATING_DOWN:
                reviews = destinationReviewRepository.findAllByDestinationIdOrderByRatingAsc(destinationId, pageable); // 별점 낮은 순
                break;
            case NEWEST:
                reviews = destinationReviewRepository.findAllByDestinationIdOrderByCreatedAtDesc(destinationId, pageable); // 최신순
                break;
            case RECOMMEND:
                reviews = destinationReviewRepository.findAllByDestinationIdOrderByRecommendCountDesc(destinationId, pageable); // 추천순
                break;
            default:
                reviews = destinationReviewRepository.findAllByDestinationId(destinationId, pageable);
                break;
        }

        log.info("[DestinationReview] 여행지 ID로 리뷰 목록 조회 완료 - 여행지 ID: {}, 총 리뷰 수: {}", destinationId, reviews.getTotalElements());
        return new CourseMakerPagination<>(pageable, reviews, reviews.getTotalElements());
    }

    @Override
    public Double getAverageRating(Long destinationId) {
        log.info("[DestinationReviewService] 여행지 평균 평점 계산 시작 - 여행지 ID: {}", destinationId);
        List<DestinationReview> reviews = destinationReviewRepository.findByDestinationId(destinationId);

        log.debug("Debug: 리뷰 수 = {}", reviews.size());

        for (DestinationReview review : reviews) {
            log.debug("Debug: 리뷰 ID = {}, 평점 = {}", review.getId(), review.getRating());
        }

        double averageRating = reviews.stream().mapToDouble(DestinationReview::getRating).average().orElse(0.0);

        DecimalFormat df = new DecimalFormat("#.#");
        double formattedAverageRating = Double.parseDouble(df.format(averageRating));

        log.info("[DestinationReview] 여행지 평균 평점 계산 완료 - 여행지 ID: {}, 평균 평점: {}", destinationId, formattedAverageRating);

        return formattedAverageRating;
    }


    @Override
    public Integer getReviewCount(Long destinationId) {
        log.info("[DestinationReview] 여행지 리뷰 개수 조회 시작 - 여행지 ID: {}", destinationId);
        int reviewCount = destinationReviewRepository.countByDestinationId(destinationId);
        log.info("[DestinationReview] 여행지 리뷰 개수 조회 완료 - 여행지 ID: {}, 리뷰 개수: {}", destinationId, reviewCount);
        return reviewCount;
    }

    @Override
    public CourseMakerPagination<DestinationReview> findByMemberNickname(String nickname, Pageable pageable) {
        log.info("[DestinationReview] 여행지 리뷰 멤버 닉네임으로 리뷰 조회 - 닉네임: {}", nickname);
        Page<DestinationReview> page = destinationReviewRepository.findByMemberNicknameAndDeletedAtIsNull(nickname, pageable);
        long total = page.getTotalElements();
        return new CourseMakerPagination<>(pageable, page, total);
    }
}