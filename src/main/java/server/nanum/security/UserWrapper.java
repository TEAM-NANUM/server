package server.nanum.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserWrapper implements UserDetails {
    private final server.nanum.domain.User user;

    private final List<GrantedAuthority> authorities;

    public UserWrapper(server.nanum.domain.User user) {
        this.user = user;
        this.authorities = List.of(new SimpleGrantedAuthority(String.valueOf(user.getUserRole())));
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
        return true; // 계정 만료 여부를 결정하는 로직에 맞게 변경해야 합니다.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부를 결정하는 로직에 맞게 변경해야 합니다.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증서 만료 여부를 결정하는 로직에 맞게 변경해야 합니다.
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부를 결정하는 로직에 맞게 변경해야 합니다.
    }
}

