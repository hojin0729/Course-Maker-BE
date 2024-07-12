package coursemaker.coursemaker.domain.auth.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*인증용 객체*/
@RequiredArgsConstructor
public class CustomMemberDetails implements UserDetails {

    private final Member member;

    /*권한 가져오기*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add((GrantedAuthority) () -> member.getRoles()
                .name());

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getNickname();
    }

    /*계정 만료 여부*/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*계정 잠김 여부*/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*자격증명(암호)가 만료됬는지 여부*/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*계정 사용 가능 여부*/
    @Override
    public boolean isEnabled() {
        return true;
    }
}
