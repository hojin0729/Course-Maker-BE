package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.repository.DestinationWishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationWishServiceImpl implements DestinationWishService {

    private DestinationWishRepository destinationWishRepository;
    private DestinationRepository destinationRepository;
    private MemberRepository memberRepository;

    public DestinationWishServiceImpl(DestinationWishRepository destinationWishRepository,
                                      DestinationRepository destinationRepository,
                                      MemberRepository memberRepository) {
        this.destinationWishRepository = destinationWishRepository;
        this.destinationRepository = destinationRepository;
        this.memberRepository = memberRepository;
    }

    /* 코스 찜목록 전체조회*/
    @Override
    public List<DestinationWishResponseDto> getAllDestinationWishes() {
        List<DestinationWish> wishes = destinationWishRepository.findAll();
        return wishes.stream()
                .map(wish -> new DestinationWishResponseDto(
                        wish.getId(),
                        wish.getDestination().getId(),
                        wish.getDestination().getName(),
                        wish.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 코스 찜목록 닉네임으로 조회 */
    @Override
    public List<DestinationWishResponseDto> getDestinationWishesByNickname(String nickname) {
        List<DestinationWish> destinationWishes = destinationWishRepository.findByMember_Nickname(nickname);
        if (destinationWishes.isEmpty()) {
            throw new RuntimeException("해당 코스 찜 정보가 없습니다.");
        }
        return destinationWishes.stream()
                .map(wish -> new DestinationWishResponseDto(
                        wish.getId(),
                        wish.getDestination().getId(),
                        wish.getDestination().getName(),
                        wish.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 코스 찜하기 */
    @Override
    @Transactional
    public DestinationWishResponseDto addDestinationWish(DestinationWishRequestDto requestDto) {
        Destination destination = destinationRepository.findById(requestDto.getDestinationId())
                .orElseThrow(() -> new RuntimeException("해당 코스를 찾을 수 없습니다."));
        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new RuntimeException("해당 닉네임을 가진 사용자가 존재하지 않습니다."));

        DestinationWish destinationWish = new DestinationWish();
        destinationWish.setDestination(destination);
        destinationWish.setMember(member);

        DestinationWish savedWish = destinationWishRepository.save(destinationWish);
        return new DestinationWishResponseDto(
                savedWish.getId(),
                savedWish.getDestination().getId(),
                savedWish.getDestination().getName(),
                savedWish.getMember().getNickname());
    }


    /* 찜하기 취소 */
    @Override
    @Transactional
    public void cancelDestinationWish(Long destinationId, String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("해당 닉네임을 가진 사용자가 존재하지 않습니다."));

        DestinationWish destinationWish = destinationWishRepository.findByDestinationIdAndMemberId(destinationId, member.getId())
                .orElseThrow(() -> new RuntimeException("해당 찜하기가 존재하지 않습니다."));

        destinationWishRepository.delete(destinationWish);
    }
}
