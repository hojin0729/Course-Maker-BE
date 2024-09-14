package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.auth.service.AuthService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.*;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
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

}