package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CourseMemberResponse {
    private String nickname;

    public CourseMemberResponse(Member member) {
        this.nickname = member.getNickname();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .build();
    }
}