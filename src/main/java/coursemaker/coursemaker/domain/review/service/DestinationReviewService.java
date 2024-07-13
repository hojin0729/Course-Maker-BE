package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;

public interface DestinationReviewService {
    // 여행지 리뷰 등록
    DestinationReview save(RequestDestinationDto requestDestinationReviewDto, Long destinationId);
    // 여행지 리뷰 수정
    DestinationReview update(Long destinationId, RequestDestinationDto requestDestinationReviewDto, String nickname);
    // 여행지 리뷰 삭제
    void delete(Long id);
    // id 기반으로 특정 여행지 리뷰 조회하는 메서드
    DestinationReview findById(Long id);
    // 여행지 리뷰 전체 보기
    CourseMakerPagination<DestinationReview> findAll(Pageable pageable);
}