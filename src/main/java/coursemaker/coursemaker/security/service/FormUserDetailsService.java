package coursemaker.coursemaker.security.service;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

//커스텀 순서 AF -> AM -> AP -> US
@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member account = memberRepository.findByUsername(username); //DB에서 계정 있는지 확인
        if (account == null) { //해당 username 정보 없으면 예외발생시
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles())); //권한설정
        ModelMapper mapper = new ModelMapper();
        AccountDto accountDto = mapper.map(account, AccountDto.class);//AccountDto에 account 정보를 복사

        return new AccountContext(accountDto, authorities);//AccountContext는 UserDetails를 구현한 클래스로 AccountDto를 Wrapping함
    }
}
