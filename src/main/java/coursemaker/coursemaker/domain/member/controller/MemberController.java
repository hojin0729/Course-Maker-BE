package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.member.dto.AccountCreateDto;
import coursemaker.coursemaker.domain.member.dto.AccountDto;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value="/signup")
    public String signup(AccountCreateDto accountCreateDto) {
        accountCreateDto.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
        memberService.createMember(accountCreateDto);
        return "redirect:/";
    }
}
