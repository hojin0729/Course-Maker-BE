package coursemaker.coursemaker.domain.like.service;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.exception.DestinationNotFoundException;
import coursemaker.coursemaker.domain.destination.repository.DestinationRepository;
import coursemaker.coursemaker.domain.like.dto.DestinationLikeRequestDto;
import coursemaker.coursemaker.domain.like.dto.DestinationLikeResponseDto;
import coursemaker.coursemaker.domain.like.entity.DestinationLike;
import coursemaker.coursemaker.domain.like.exception.DestinationLikeNotFoundException;
import coursemaker.coursemaker.domain.like.exception.DuplicateLikeException;
import coursemaker.coursemaker.domain.like.repository.DestinationLikeRepository;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationLikeServiceImpl implements DestinationLikeService {

    private DestinationLikeRepository destinationLikeRepository;
    private DestinationRepository destinationRepository;
    private MemberRepository memberRepository;

    public DestinationLikeServiceImpl(DestinationLikeRepository destinationLikeRepository,
                                      DestinationRepository destinationRepository,
                                      MemberRepository memberRepository) {
        this.destinationLikeRepository = destinationLikeRepository;
        this.destinationRepository = destinationRepository;
        this.memberRepository = memberRepository;
    }

    /* 목적지 좋아요목록 전체조회*/
    @Override
    public List<DestinationLikeResponseDto> getAllDestinationLikes() {

        List<DestinationLike> likes = destinationLikeRepository.findAll();

        if (likes.isEmpty()) {
            throw new DestinationLikeNotFoundException("목적지 좋아요가 존재하지 않습니다.", "Invalid like");
        }


        return likes.stream()
                .map(like -> new DestinationLikeResponseDto(
                        like.getDestination().getId(),
                        like.getDestination().getName(),
                        like.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 목적지 좋아요목록 닉네임으로 조회 */
    @Override
    public List<DestinationLikeResponseDto> getDestinationLikesByNickname(String nickname) {
        List<DestinationLike> destinationLikes = destinationLikeRepository.findByMember_Nickname(nickname);
        if (destinationLikes.isEmpty()) {
            throw new DestinationLikeNotFoundException("목적지 좋아요가 존재하지 않습니다.", "Nickname: " + nickname);
        }
        return destinationLikes.stream()
                .map(like -> new DestinationLikeResponseDto(
                        like.getDestination().getId(),
                        like.getDestination().getName(),
                        like.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 목적지 좋아요하기 */
    @Override
    @Transactional
    public DestinationLikeResponseDto addDestinationLike(DestinationLikeRequestDto requestDto) {

        Destination destination = destinationRepository.findById(requestDto.getDestinationId())
                .orElseThrow(() -> new DestinationNotFoundException("해당 목적지를 찾을 수 없습니다.", "DestinationId:" + requestDto.getDestinationId()));

        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + requestDto.getNickname()));

        // 중복 체크 로직 추가
        boolean exists = destinationLikeRepository.existsByDestinationIdAndMemberId(destination.getId(), member.getId());
        if (exists) {
            throw new DuplicateLikeException("이미 이 목적지를 좋아요했습니다.", "DestinationId: " + destination.getId() + ", Nickname: " + member.getNickname());
        }

        DestinationLike destinationLike = new DestinationLike();
        destinationLike.setDestination(destination);
        destinationLike.setMember(member);

        DestinationLike savedLike = destinationLikeRepository.save(destinationLike);
        return new DestinationLikeResponseDto(
                savedLike.getDestination().getId(),
                savedLike.getDestination().getName(),
                savedLike.getMember().getNickname());
    }


    /* 목적지 좋아요하기 취소 */
    @Override
    @Transactional
    public void cancelDestinationLike(Long destinationId, String nickname) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 사용자가 존재하지 않습니다.", "Nickname: " + nickname));

        // 좋아요 정보 가져오기
        DestinationLike destinationLike = destinationLikeRepository.findByDestinationIdAndMemberId(destinationId, member.getId())
                .orElseThrow(() -> new DestinationLikeNotFoundException("해당 목적지 좋아요가 존재하지 않습니다.", "destinationId: " + destinationId + ", Nickname: " + nickname));

        // 목적지 좋아요 삭제
        destinationLikeRepository.delete(destinationLike);
    }

    /* 특정 목적지에 대한 좋아요 목록 조회 */
    @Override
    public List<DestinationLikeResponseDto> getLikesByDestinationId(Long destinationId) {
        List<DestinationLike> destinationLikes = destinationLikeRepository.findByDestinationId(destinationId);
        if (destinationLikes.isEmpty()) {
            throw new DestinationLikeNotFoundException("해당 목적지에 대한 좋아요가 존재하지 않습니다.", "DestinationId: " + destinationId);
        }
        return destinationLikes.stream()
                .map(like -> new DestinationLikeResponseDto(
                        like.getDestination().getId(),
                        like.getDestination().getName(),
                        like.getMember().getNickname()))
                .collect(Collectors.toList());
    }


    /* 목적지별 좋아요된 수 조회 */
    @Override
    public Integer getDestinationLikeCount(Long destinationId) {
        // 목적지가 존재하는지 확인
        destinationRepository.findById(destinationId)
                .orElseThrow(() -> new DestinationNotFoundException("해당 목적지를 찾을 수 없습니다.", "DestinationId: " + destinationId));

        // 목적지에 대한 좋아요 수 반환
        return destinationLikeRepository.countByDestinationId(destinationId);
    }

}
