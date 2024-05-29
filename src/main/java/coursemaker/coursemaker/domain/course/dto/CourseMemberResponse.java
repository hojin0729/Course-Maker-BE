package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class CourseMemberResponse {
    private final Long id;
    private final String username;
    private final int age;
    private final String roles;

    public CourseMemberResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.age = member.getAge();
        this.roles = member.getRoles();
    }
}