package coursemaker.coursemaker.domain.auth.service;

import coursemaker.coursemaker.domain.auth.dto.JoinRequestDto;
import coursemaker.coursemaker.domain.auth.dto.JoinResponseDto;
import coursemaker.coursemaker.domain.auth.dto.LoginRequestDto;
import coursemaker.coursemaker.domain.auth.dto.LoginResponseDto;
import coursemaker.coursemaker.domain.auth.exception.InvalidPasswordException;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

    // TODO: 로그인 로직 개발
    // TODO: 시큐리티의 로그인/로그아웃 VS 직접 만드는 로그인/로그아웃
    public LoginResponseDto login(LoginRequestDto request){
        LoginResponseDto response = new LoginResponseDto();

        /*회원 존재여부 검증*/
        Member member = memberRepository.findByEmail(request.getLoginEmail())
                .orElseThrow(() -> new UserNotFoundException("해당 회원을 찾을 수 없습니다. ", "Email: " + request.getLoginEmail()));

        /*비밀번호 일치 여부 검증*/
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.", "Password: " + request.getPassword());
        }

        String access;

        access = jwtProvider.createAccessToken(member.getNickname(), member.getRoles().name());
        return response;
    }

}
