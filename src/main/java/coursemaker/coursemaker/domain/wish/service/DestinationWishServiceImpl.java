package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.exception.DestinationWishNotFoundException;
import coursemaker.coursemaker.domain.wish.repository.DestinationWishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    /* 목적지 찜목록 전체조회*/
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


    /* 목적지 찜목록 닉네임으로 조회 */
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


    /* 목적지 찜하기 */
    @Override
    @Transactional
    public DestinationWishResponseDto addDestinationWish(DestinationWishRequestDto requestDto) {

        Destination destination = destinationRepository.findById(requestDto.getDestinationId())
                .orElseThrow(() -> new DestinationNotFoundException("해당 목적지를 찾을 수 없습니다.", "DestinationId:" + requestDto.getDestinationId()));

        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + requestDto.getNickname()));

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


    /* 목적지 찜하기 취소 */
    @Override
    @Transactional
    public void cancelDestinationWish(Long destinationId, String nickname) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + nickname));

        // 찜 정보 가져오기
        DestinationWish destinationWish = destinationWishRepository.findByDestinationIdAndMemberId(destinationId, member.getId())
                .orElseThrow(() -> new DestinationWishNotFoundException("해당 목적지 찜이 존재하지 않습니다.", "destinationId: " + destinationId + ", Nickname: " + nickname));

        // 목적지 찜 삭제
        destinationWishRepository.delete(destinationWish);
    }
}
