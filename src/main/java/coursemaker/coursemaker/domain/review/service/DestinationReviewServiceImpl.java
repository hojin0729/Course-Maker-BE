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
            // 디버깅: 저장 전 리뷰 평점 출력
            log.debug("Debug: Saving Review with Rating = " + destinationReview.getRating());
            return destinationReviewRepository.save(destinationReview);
        } catch (DataIntegrityViolationException e) {
            // 유니크 제약 조건 위반 시 사용자 정의 예외 던지기
            throw new DuplicatedReviewException("해당 여행지에 이미 리뷰를 남겼습니다.",
                    "[DestinationReview] nickname: " + member.getNickname() + ", destination id: " + destinationId);
        }
    }


    @Override
    public DestinationReview update(Long destinationId, @Valid RequestDestinationDto requestDestinationDto, String nickname) {
        Member member = memberService.findByNickname(nickname);
        Destination destination = destinationService.findById(destinationId);

        DestinationReview existingReview = destinationReviewRepository.findByMemberAndDestination(member, destination)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] nickname: " + nickname));

        existingReview.setTitle(requestDestinationDto.getTitle());
        existingReview.setDescription(requestDestinationDto.getDescription());
        existingReview.setPicture(requestDestinationDto.getPicture());
        existingReview.setRating(requestDestinationDto.getRating());

        return destinationReviewRepository.save(existingReview);
    }

    @Override
    public void delete(Long id) {
        destinationReviewRepository.findById(id).orElseThrow(() ->
                new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] delete id: "+ id)
        );
        destinationReviewRepository.deleteById(id);
    }

    @Override
    public DestinationReview findById(Long id) {
        return destinationReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다.", "[DestinationReview] id: "+ id));
    }


    @Override
    public CourseMakerPagination<DestinationReview> findAllByDestinationId(Long destinationId, Pageable pageable) {
        Destination destination = destinationService.findById(destinationId);
        Page<DestinationReview> page = destinationReviewRepository.findByDestination(destination, pageable);
        return new CourseMakerPagination<>(pageable, page, page.getTotalElements());
    }

    @Override
    public Double getAverageRating(Long destinationId) {
        List<DestinationReview> reviews = destinationReviewRepository.findByDestinationId(destinationId);

        log.debug("Debug: Number of reviews found = " + reviews.size());

        for (DestinationReview review : reviews) {
            log.debug("Debug: Review ID = " + review.getId() + ", Rating = " + review.getRating());
        }

        double averageRating = reviews.stream().mapToDouble(DestinationReview::getRating).average().orElse(0.0);

        DecimalFormat df = new DecimalFormat("#.#");
        double formattedAverageRating = Double.parseDouble(df.format(averageRating));

        log.debug("Debug: Calculated formatted averageRating = " + formattedAverageRating);

        return formattedAverageRating;
    }


    @Override
    public Integer getReviewCount(Long destinationId){
        return destinationReviewRepository.countByDestinationId(destinationId);
    }
}