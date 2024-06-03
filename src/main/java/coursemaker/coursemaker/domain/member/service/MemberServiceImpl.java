package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.member.dto.AccountCreateDto;
import coursemaker.coursemaker.domain.member.dto.AccountDto;
import coursemaker.coursemaker.domain.member.dto.AccountUpdateDto;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountDto createMember(AccountCreateDto request) {
        Member member = modelMapper.map(request, Member.class);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member = memberRepository.save(member);
        return modelMapper.map(member, AccountDto.class);
    }

    @Override
    public List<AccountDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> modelMapper.map(member, AccountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        return modelMapper.map(member, AccountDto.class);
    }

    @Override
    public AccountDto updateMember(Long id, AccountUpdateDto request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        modelMapper.map(request, member);
        member = memberRepository.save(member);
        return modelMapper.map(member, AccountDto.class);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}