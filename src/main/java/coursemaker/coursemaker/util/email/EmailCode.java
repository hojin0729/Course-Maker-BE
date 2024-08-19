package coursemaker.coursemaker.util.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "email_code")
public class EmailCode {
    @Id
    @Column(name = "email")
    private String id; // 수신자의 이메일(toEmail)을 받음

    @Column(name = "validateCode")
    private String validateCode;

    private LocalDateTime expireTime;

    protected EmailCode(){}

}
