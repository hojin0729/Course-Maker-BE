package coursemaker.coursemaker.domain.member.entity;

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
    @Column
    private String username; //회원 id
    @Column
    private String name; //회원 이름
    @Column
    private String email; // 회원 이메일
    @Column
    private String nickname; //회원 닉네임
    @Column
    private String phoneNumber; //회원 전화번호
    @Column
    private String password; //회원 비밀번호
    @Column
    private String roles; //회원 등급

}