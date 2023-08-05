package server.nanum.security.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import server.nanum.domain.User;
import server.nanum.repository.UserRepository;


/**
 * 사용자 상세 정보 서비스 클래스
 * 사용자의 상세 정보를 로드하는 서비스입니다. UserDetailsService 인터페이스를 구현합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    /**
     * 사용자 이름을 기준으로 사용자 상세 정보를 로드
     * @param username 사용자 이름
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 경우 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // UserWrapper 객체를 만들어 반환
        return new UserWrapper(user);
    }
}



