package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.review.dto.RequestDestinationDto;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.repository.DestinationReviewRepository;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        DestinationReview destinationReview = requestDestinationDto.toEntity(member);
        destinationReview.setDestination(destination);
        return destinationReviewRepository.save(destinationReview);
    }

    @Override
    public DestinationReview update(Long destinationId, @Valid RequestDestinationDto requestDestinationDto, String nickname) {
        Member member = memberService.findByNickname(nickname);
        Destination destination = destinationService.findById(destinationId);

        DestinationReview existingReview = destinationReviewRepository.findByMemberAndDestination(member, destination)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. destinationId: " + destinationId + ", nickname: " + nickname));

        existingReview.setTitle(requestDestinationDto.getTitle());
        existingReview.setDescription(requestDestinationDto.getDescription());
        existingReview.setPicture(requestDestinationDto.getPicture());
        existingReview.setRating(requestDestinationDto.getRating());

        return destinationReviewRepository.save(existingReview);
    }

    @Override
    public void delete(Long id) {
        destinationReviewRepository.deleteById(id);
    }

    @Override
    public DestinationReview findById(Long id) {
        return destinationReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. id: " + id));
    }

    @Override
    public CourseMakerPagination<DestinationReview> findAll(Pageable pageable) {
        Page<DestinationReview> page = destinationReviewRepository.findAll(pageable);
        // CourseMakerPagination<DestinationReview> courseMakerPagination = new CourseMakerPagination<>(pageable, page, total)
        return null;
    }
}