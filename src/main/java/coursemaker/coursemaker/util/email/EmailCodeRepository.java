package coursemaker.coursemaker.util.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, String> {
}
