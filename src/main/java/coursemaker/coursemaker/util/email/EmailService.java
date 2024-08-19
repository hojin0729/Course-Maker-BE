package coursemaker.coursemaker.util.email;

import coursemaker.coursemaker.config.EmailConfig;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;
    private String fromEmail;

    @PostConstruct
    private void init() {
        fromEmail = emailConfig.getUsername();
    }

    public void sendMail(String toEmail, String title, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); // JavaMailSender 객체를 이용해 MimeMessage 객체 생성

        try {
            mimeMessage.addRecipients(MimeMessage.RecipientType.TO, toEmail);
            mimeMessage.setSubject(title);
            mimeMessage.setFrom(fromEmail);
            mimeMessage.setText(content, "utf-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        log.info("[Email]이메일 전송. send to: {}", toEmail);
        javaMailSender.send(mimeMessage);
    }
}
