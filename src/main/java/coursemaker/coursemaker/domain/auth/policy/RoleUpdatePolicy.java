package coursemaker.coursemaker.domain.auth.policy;

import coursemaker.coursemaker.domain.auth.dto.role.RoleUpdateDTO;
import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.review.service.CourseReviewService;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleUpdatePolicy {

    private final AuthService authService;
    /*
    * 순환참조 막기 위해 임시로 lazy 로딩 함.
    * TODO: 배치로 돌리기
    *  */
    @Lazy
    private final CourseReviewService courseReviewService;

    @Lazy
    private final DestinationReviewService destinationReviewService;

    public boolean isUpdatable(Member member) {

        /*초보 여행가 -> 중급 여행가 등업 여부*/
        if(member.getRoles() == Role.BEGINNER_TRAVELER){

            int reviewCnt = 0;

//        reviewCnt += courseReviewService.findAllByNickname().size();
//        reviewCnt += destinationReviewService.findAllByNickname().size();

            if(reviewCnt>=50){
                return true;
            }
        } else if(member.getRoles() == Role.INTERMEDIATE_TRAVELER){
            return false;
        }

        return false;
    }

    private Role findRoleToFit(Member member) {

        if(!isUpdatable(member)){
            return member.getRoles();
        }

        /*초보 여행가 -> 중급 여행가 등업 여부*/
        if(member.getRoles() == Role.BEGINNER_TRAVELER){

            int reviewCnt = 0;

//        reviewCnt += courseReviewService.findAllByNickname().size();
//        reviewCnt += destinationReviewService.findAllByNickname().size();

            if(reviewCnt>=50){
                log.debug("[AUTH] 등업 권한 충족. 닉네임: {}, 등업: {} -> {}",
                        member.getNickname(),
                        member.getRoles().getRole(),
                        Role.INTERMEDIATE_TRAVELER.getRole()
                );

                return Role.INTERMEDIATE_TRAVELER;
            }
        } else if(member.getRoles() == Role.INTERMEDIATE_TRAVELER){
            return Role.INTERMEDIATE_TRAVELER;
        }

        return member.getRoles();
    }

    @Transactional
    public Role updateRoleToFit(Member member) {

        /*등급을 업데이트 할 수 있으면*/
        if(isUpdatable(member)) {
            Role fitRole = findRoleToFit(member);

            RoleUpdateDTO roleUpdateDTO = new RoleUpdateDTO();
            roleUpdateDTO.setRole(fitRole);
            roleUpdateDTO.setNickname(member.getNickname());

            authService.updateRole(roleUpdateDTO);

            return fitRole;
        }

        return member.getRoles();
    }

}
