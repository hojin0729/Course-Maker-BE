package coursemaker.coursemaker.domain.auth.service;

import coursemaker.coursemaker.domain.auth.dto.*;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
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
    private final JwtProvider jwtProvider;

    /*회원가입*/
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

    /*토큰 재발행*/
    public ReIssueResponseDto reissueToken(ReIssueRequestDto request){
        ReIssueResponseDto response = new ReIssueResponseDto();

        response.setAccessToken(jwtProvider.reIssue(request.getRefreshToken()) );

        log.info("[Auth] 토큰 재발행. 토큰: {}", response.getAccessToken());
        return response;
    }

    /*회원 탈퇴*/
    public void withdrawal(String nickname) {

        if(nickname == null) {
            throw new IllegalArgumentException("닉네임은 null 값이 들어갈 수 없습니다. 로그인 혹은 인자값을 확읺주시요.");
        }

        Member member = memberRepository.findByNickname(nickname).orElseThrow(() ->
                new UserNotFoundException("존재하지 않는 회원입니다.", "Nickname: " + nickname)
        );

        System.out.println("xxxxxx member = " + member);

        memberRepository.delete(member);

        log.info("[Auth] 회원 탈퇴. 닉네임: {}", member.getNickname());
    }

}
