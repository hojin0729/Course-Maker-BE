package coursemaker.coursemaker.domain.auth.service;

import coursemaker.coursemaker.domain.auth.dto.join_withdraw.JoinRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.join_withdraw.JoinResponseDTO;
import coursemaker.coursemaker.domain.auth.dto.jwt.ReIssueRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.jwt.ReIssueResponseDTO;
import coursemaker.coursemaker.domain.auth.dto.validate.SendValidateCodeRequestDTO;
import coursemaker.coursemaker.domain.auth.dto.validate.ValidateEmailRequestDTO;
import coursemaker.coursemaker.domain.auth.exception.TimeOutValidationException;
import coursemaker.coursemaker.domain.auth.exception.UnMatchValidateCodeException;
import coursemaker.coursemaker.domain.auth.exception.UnSendEmailException;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.util.email.EmailCode;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import coursemaker.coursemaker.util.email.EmailCodeRepository;
import coursemaker.coursemaker.util.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final EmailCodeRepository emailCodeRepository;

    /*회원가입*/
    public JoinResponseDTO join(JoinRequestDTO request){
        JoinResponseDTO response = new JoinResponseDTO();
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
    public ReIssueResponseDTO reissueToken(ReIssueRequestDTO request){
        ReIssueResponseDTO response = new ReIssueResponseDTO();

        response.setAccessToken(jwtProvider.reIssue(request.getRefreshToken()) );

        log.info("[Auth] 토큰 재발행. 토큰: {}", response.getAccessToken());
        return response;
    }

    /*회원 탈퇴*/
    public void withdrawal(String nickname) {

        if(nickname == null) {
            throw new IllegalArgumentException("로그인 상태를 확인해주세요.");
        }

        Member member = memberRepository.findByNickname(nickname).orElseThrow(() ->
                new UserNotFoundException("존재하지 않는 회원입니다.", "Nickname: " + nickname)
        );

        memberRepository.delete(member);

        log.info("[Auth] 회원 탈퇴. 닉네임: {}", member.getNickname());
    }

    /*닉네임 검증*/
    public NicknameValidate validateNickname(String nickname) {

        /*닉네임 정책에 적합한지 판별*/
        String pattern = "^[a-z0-9가-힣ㄱ-ㅎ]{2,10}$";
        boolean isMatches = Pattern.matches(pattern, nickname);
        if(!isMatches) {
            return NicknameValidate.IS_NOT_MATCH_PATTERN;
        }

        /*닉네임 존재 여부 판별*/
        boolean isExist = memberRepository.findByNickname(nickname).isPresent();
        if(!isExist) {
            return NicknameValidate.IS_NOT_EXIST;
        } else{
            return NicknameValidate.IS_EXIST;
        }
    }

    /*이메일 인증 코드 전송*/
    public String sendValidationCode(SendValidateCodeRequestDTO dto) {

        memberRepository.findByEmail(dto.getEmail()).ifPresent(m -> {
            throw new UserDuplicatedException("이미 존재하는 이메일 입니다.", "duplicate email validate: "+ dto.getEmail());
        });

        String validateCode = generateValidateCode();
        String title = "Course Maker 이메일 인증코드";
        String content =
                "<h1>CourseMaker 방문해주셔서 감사합니다.</h1><br><br>"
                        + "인증 코드는 <b>" + validateCode + "</b>입니다.<br>"
                        + "인증 코드를 바르게 입력해주세요."
                ;

        emailService.sendMail(
                dto.getEmail(),
                title,
                content
        );

        EmailCode emailCode = new EmailCode(dto.getEmail(), validateCode, LocalDateTime.now().plusMinutes(3L));
        log.info("이메일 시간: {}", LocalDateTime.now().plusMinutes(3L));

        emailCodeRepository.save(emailCode);

        return validateCode;
    }

    public void validateEmail(ValidateEmailRequestDTO dto){
        EmailCode code = emailCodeRepository.findById(dto.getEmail()).orElseThrow(() ->
                new UnSendEmailException("전송되지 않은 이메일 입니다.", "이메일 전송 기록이 없음. email: "+dto.getEmail()));

        /*전송된 코드와 일치하지 않을때*/
        if(!code.getValidateCode().equals(dto.getCode())){
            throw new UnMatchValidateCodeException("이메일 코드가 일치하지 않습니다.", "인증코드 불일치: "+ dto.getCode());
        }

        /*시간초과*/
        if(code.getExpireTime().isBefore(LocalDateTime.now())){
            emailCodeRepository.delete(code);
            throw new TimeOutValidationException("인증 시간이 초과됬습니다. 다시 이메일을 전송해주세요.", "[Email] timeout. email: "+code.getExpireTime() + " current time: "+ LocalDateTime.now());
        }


        /*검증 끝났으면 삭제함.*/
        emailCodeRepository.delete(code);
    }

    private String generateValidateCode(){
        UUID uuid = UUID.randomUUID();

        return uuid.toString().split("-")[0];
    }


}
