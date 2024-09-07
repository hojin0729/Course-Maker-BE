package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.*;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    public Member findById(Long userId){

        Member result = memberRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));

        /*관리자의 경우*/
        if(authService.getLoginInfo()!=null && authService.getLoginInfo().getRole()== Role.ADMIN){
            return result;
        }

        /*관리자가 아닌 경우*/
        if(result.getDeletedAt() == null){
            return result;
        } else{
            throw new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId);
        }

    }

    public Member findByNickname(String nickname) {
        Member result = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));

        /*관리자의 경우*/
        if(authService.getLoginInfo()!=null && authService.getLoginInfo().getRole()== Role.ADMIN){
            return result;
        }

        /*관리자가 아닌 경우*/
        if(result.getDeletedAt() == null){
            return result;
        } else{
            throw new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + nickname);
        }
    }

    public Member findByEmail(String email){
        Member result = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Email: " + email));

        /*관리자의 경우*/
        if(authService.getLoginInfo()!=null && authService.getLoginInfo().getRole()== Role.ADMIN){
            return result;
        }

        /*관리자가 아닌 경우*/
        if(result.getDeletedAt() == null){
            return result;
        } else{
            throw new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + email);
        }
    }

//    /*내가 만든 여행지 반환*/
//    public CourseMakerPagination<CourseDestinationResponse> getMyDestinations(String nickanme, Pageable pageable){
//        CourseMakerPagination<Destination> destinationPage = destinationService.findByMemberNickname(nickanme, pageable);
//
//
//        List<DestinationDto> contents = destinationPage.getContents().stream()
//                .map(destination -> {
//                    Boolean isMyDestination = true;
//                    Boolean isMyWishDestination = destinationWishService.isDestinationWishedByUser(destination.getId(), nickanme);
//                    Boolean isMyLikeDestination = loginedInfo != null && destinationLikeService.isDestinationLikedByUser(destination.getId(), loginedInfo.getNickname());
//                    List<TagResponseDto> tags = tagService.findAllByDestinationId(destination.getId());
//                    Double averageRating = destinationReviewService.getAverageRating(destination.getId());
//                    Integer reviewCount = destinationReviewService.getReviewCount(destination.getId());
//                    Integer wishCount = destinationWishService.getDestinationWishCount(destination.getId());
//                    Integer likeCount = destinationLikeService.getDestinationLikeCount(destination.getId());
//                    return DestinationDto.toDto(destination, tags, destination.getIsApiData(), averageRating, isMyDestination, reviewCount, wishCount, likeCount, isMyWishDestination, isMyLikeDestination);
//                })
//                .toList();
//
//        Page<DestinationDto> responsePage = new PageImpl<>(contents, pageable, destinationPage.getTotalPage());
//        CourseMakerPagination<DestinationDto> response = new CourseMakerPagination<>(pageable, responsePage, destinationPage.getTotalContents());
//
//    }


//    public Member updateUser(UpdateRequest updateRequest, String nickname) {
//        updateRequest.validate(); // 검증 로직 추가
//        //TODO: 수정 시 Access Token 재발급
//        Member user = memberRepository.findByNickname(nickname)
//                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));
//
//        if (updateRequest.getNickname() != null && !updateRequest.getNickname().equals(nickname)) {
//            if (memberRepository.findByNickname(updateRequest.getNickname()).isPresent()) {
//                throw new UserDuplicatedException("이미 존재하는 닉네임입니다.", "Nickname: " + updateRequest.getNickname());
//            }
//            user.setNickname(updateRequest.getNickname());
//        }
//
//        if (updateRequest.getName() != null) {
//            user.setName(updateRequest.getName());
//        }
//        if (updateRequest.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
//        }
////        if (updateRequest.getProfileImgUrl() != null) {
////            user.setProfileImgUrl(updateRequest.getProfileImgUrl());
////        }
////        if (updateRequest.getProfileDescription() != null) {
////            user.setProfileDescription(updateRequest.getProfileDescription());
////        }
//
//        return memberRepository.save(user);
//    }
//
//    public Member deleteUser(DeleteRequest deleteRequest) {
//        if (deleteRequest == null || deleteRequest.getUserId() == null) {
//            throw new IllegalUserArgumentException("유효하지 않은 요청입니다. 유저 ID가 필요합니다.", "deleteRequest or userId is null");
//        }
//
//        Long userId = deleteRequest.getUserId();
//
//        Member user = memberRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "ID: " + userId));
//
//        user.setDeletedAt(LocalDateTime.now());
//
//        try {
//            Member deletedUser = memberRepository.save(user);
//            return deletedUser;
//        } catch (Exception e) {
//            log.error("Error occurred while deleting user with ID: {}", userId, e);
//            throw new RuntimeException("회원 삭제 중 오류가 발생했습니다.");
//        }
//    }
//
//    public MyPageResponse showMyPage(String nickname) {
//        Member currentUser = memberRepository.findByNickname(nickname)
//                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Nickname: " + nickname));
//
//        String name = currentUser.getName();
//        String profileDescription = currentUser.getProfileDescription();
//        String profileImgUrl = currentUser.getProfileImgUrl();
//
//        MyPageResponse myPageResponse = MyPageResponse.builder()
//                .nickname(nickname)
//                .name(name)
//                .profileDescription(profileDescription)
//                .profileImgUrl(profileImgUrl)
//                .build();
//
//        return myPageResponse;
//    }


}