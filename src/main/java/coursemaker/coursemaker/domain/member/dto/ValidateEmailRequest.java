package coursemaker.coursemaker.domain.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ValidateEmailRequest {
    @Email
    private String email;
}
