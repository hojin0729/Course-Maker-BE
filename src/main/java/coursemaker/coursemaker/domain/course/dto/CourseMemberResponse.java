package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CourseMemberResponse {
    private final Long id;
    private final String username;
    private final String roles;

    public CourseMemberResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.roles = member.getRoles();
    }
}