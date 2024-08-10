package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.repository.DestinationWishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationWishServiceImpl implements DestinationWishService {

    private DestinationWishRepository destinationWishRepository;
    private DestinationRepository destinationRepository;
    private MemberRepository memberRepository;

    /* 코스 찜목록 전체조회*/
    @Override
    public List<DestinationWish> getAllDestinationWishes() {
        return destinationWishRepository.findAll();
    }


    /* 코스 찜목록 닉네임으로 조회 */
    @Override
    public List<DestinationWish> getDestinationWishesByNickname(String nickname) {
        List<DestinationWish> courseWishes = destinationWishRepository.findByMember_Nickname(nickname);
        if (courseWishes.isEmpty()) {
            throw new RuntimeException("해당 코스 찜 정보가 없습니다.");
        }
        return courseWishes;
    }

    /* 코스 찜하기 */
    @Override
    public DestinationWish addDestinationWish(Long destinationId, Long memberId) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("해당 코스를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));

        DestinationWish destinationWish = new DestinationWish();
        destinationWish.setDestination(destination);
        destinationWish.setMember(member);

        return destinationWishRepository.save(destinationWish);
    }

    /* 찜하기 취소 */
    @Override
    public void cancelDestinationWish(Long destinationId, Long memberId) {
        Optional<DestinationWish> optionalDestinationWish = destinationWishRepository.findByDestinationIdAndMemberId(destinationId, memberId);
        if (optionalDestinationWish.isPresent()) {
            destinationWishRepository.delete(optionalDestinationWish.get());
        } else {
            throw new RuntimeException("해당 찜하기가 존재하지 않습니다.");
        }
    }
}
