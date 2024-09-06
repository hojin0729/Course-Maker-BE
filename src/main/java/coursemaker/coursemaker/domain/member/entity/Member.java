package coursemaker.coursemaker.domain.member.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
//@SQLRestriction("deleted_at IS NULL")
//@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
@Getter
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_type", columnDefinition = "VARCHAR(255) DEFAULT 'BASIC'")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "name", columnDefinition = "VARCHAR(20)")
    private String name; // 회원 이름

    @Column(name = "email", columnDefinition = "VARCHAR(255) UNIQUE")
    private String email; // 회원 이메일

    @Column(unique = true, columnDefinition = "VARCHAR(20) UNIQUE")
    private String nickname; // 회원 닉네임

    @Column(nullable = true, length = 15)
    private String phoneNumber; // 회원 전화번호

    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password; // 회원 비밀번호

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role roles; // 회원 등급

    public Member(String nickname, String email, String name, String password, String phoneNumber, LoginType loginType, Role roles) {
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.loginType = loginType;
        this.roles = roles;
    }

    protected Member() {}


    public enum LoginType {
        BASIC,
        KAKAO
    }
}