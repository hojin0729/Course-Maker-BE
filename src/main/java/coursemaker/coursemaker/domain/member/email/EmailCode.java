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
    public EmailCode(String id, String emailCode) {
        this.id = id;
        this.emailCode = emailCode;
    }

    // authCode 생성 로직을 이곳으로 옮기는 것도 가능.
    public static EmailCode create(String email) {
        String generatedAuthCode = generateAuthCode(); // 인증 코드 생성 로직
        return EmailCode.builder()
                .id(email)
                .emailCode(generatedAuthCode)
                .build();
    }

    private static String generateAuthCode() {
        // 인증 코드 생성 로직을 여기에 구현
        // 예: 6자리 난수 생성
        int authCode = (int)(Math.random() * 1000000);
        return String.format("%06d", authCode);
    }
}
