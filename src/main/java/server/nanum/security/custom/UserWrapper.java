package server.nanum.security.custom;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import server.nanum.domain.Seller;
import server.nanum.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * 사용자 래퍼 클래스
 * 사용자 정보를 UserDetails 인터페이스에 맞게 감싸서 처리하는 클래스입니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Getter
public class UserWrapper implements UserDetails {
    private final User user;

    private final List<GrantedAuthority> authorities;

    /**
     * 사용자 래퍼 생성자
     * @param user 사용자 객체
     */
    public UserWrapper(User user) {
        this.user = user;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_"+user.getUserRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "비밀번호는 없습니다.";
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

