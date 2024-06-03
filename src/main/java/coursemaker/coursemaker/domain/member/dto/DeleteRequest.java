package coursemaker.coursemaker.domain.member.dto;

import lombok.Data;

@Data
public class DeleteRequest {
    private Long userId; //TODO: 쿠키에서 가져오기
}

