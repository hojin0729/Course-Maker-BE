package coursemaker.coursemaker.domain.member.repository;

import coursemaker.coursemaker.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String loginEmail);

    boolean existsByEmail(String email);
}
