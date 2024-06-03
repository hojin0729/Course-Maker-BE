package coursemaker.coursemaker.domain.member.entity;

import coursemaker.coursemaker.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 회원 id

    @Column(nullable = false)
    private String name; // 회원 이름

    @Column(nullable = false, unique = true)
    private String email; // 회원 이메일

    @Column(nullable = false)
    private String nickname; // 회원 닉네임

    @Column(nullable = true, length = 15)
    private String phoneNumber; // 회원 전화번호

    @Column(nullable = false)
    private String password; // 회원 비밀번호

    @Column(nullable = false)
    private String roles; // 회원 등급

}