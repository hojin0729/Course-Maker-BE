package coursemaker.coursemaker.domain.member.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "email_code")
public class EmailCode {
    @Id
    @Column(name = "email")
    private String id; // 수신자의 이메일(toEmail)을 받음

    @Column(name = "email_code")
    private String emailCode;

    // 생성자 및 빌더 패턴을 통한 객체 생성
    @Builder
    public EmailCode(String toEmail, String emailCode) {
        this.id = toEmail;
        this.emailCode = emailCode;
    }
}
