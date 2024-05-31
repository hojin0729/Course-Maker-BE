package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.member.dto.AccountCreateDto;
import coursemaker.coursemaker.domain.member.dto.AccountDto;
import coursemaker.coursemaker.domain.member.dto.AccountUpdateDto;

import java.util.List;


public interface MemberService {
    AccountDto createMember(AccountCreateDto request);
    List<AccountDto> getAllMembers();
    AccountDto getMemberById(Long id);
    AccountDto updateMember(Long id, AccountUpdateDto request);
    void deleteMember(Long id);
}
