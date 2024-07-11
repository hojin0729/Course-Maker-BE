package coursemaker.coursemaker.domain.auth.service;

import coursemaker.coursemaker.domain.auth.dto.CustomMemberDetails;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberService memberService;
    
    /*사용자 정보 인증*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        /*사용자 정봏를 DB에서 가져옴*/
        Member member = memberService.findByEmail(username);
        
        /*인증객체로 넘겨서 인증 진행*/
        return new CustomMemberDetails(member);
    }
}
