package coursemaker.coursemaker.domain.auth.service;

import coursemaker.coursemaker.domain.auth.dto.JoinRequestDto;
import coursemaker.coursemaker.domain.auth.dto.JoinResponseDto;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinResponseDto join(JoinRequestDto request){
        JoinResponseDto response = new JoinResponseDto();
        Member member = new Member();

        /*닉네임 중복 검증*/
        memberRepository.findByNickname(request.getNickname()).ifPresent(m -> {
            throw new UserDuplicatedException("이미 존재하는 닉네임입니다.", "Nickname: " + request.getNickname());
        });

        /*이메일 중복 검증*/
        memberRepository.findByEmail(request.getEmail()).ifPresent(m -> {
            throw new UserDuplicatedException("이미 존재하는 이메일입니다.", "email: " + request.getEmail());
        });


        /*DTO -> entity*/
        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setNickname(request.getNickname());
        member.setPassword(passwordEncoder.encode(request.getPassword()) );
        member.setPhoneNumber(request.getPhoneNumber());
        member.setRoles(Role.USER);

        /*DB에 회원정보 저장*/

        member = memberRepository.save(member);
        response.setNickname(member.getNickname());

        log.info("[Auth] 신규 회원가입(이메일). 닉네임: {}", member.getNickname());

        return response;
    }

}
