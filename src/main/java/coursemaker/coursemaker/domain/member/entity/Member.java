package coursemaker.coursemaker.domain.member.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_type", columnDefinition = "VARCHAR(255) DEFAULT 'BASIC'")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = true)
    private String username; // 회원 id

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role roles; // 회원 등급

    @Column(name = "profile_img_url", columnDefinition = "VARCHAR(255)")
    private String profileImgUrl;

    @Column(name = "profile_description", columnDefinition = "VARCHAR(255)")
    private String profileDescription;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;


    public enum LoginType {
        BASIC,
        KAKAO
    }

    @Builder(builderMethodName = "addMemberBuilder")
    public Member(String email, LoginType loginType, String name, String nickname, String password, String phoneNumber, String profileImgUrl, String profileDescription, String roles) {
        this.email = email;
        this.loginType = loginType;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImgUrl = profileImgUrl;
        this.profileDescription = profileDescription;
//        this.roles = roles != null ? roles : Role.USER.getRole();
    }
}