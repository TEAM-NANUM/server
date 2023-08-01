package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.security.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;

    // UserService에서 사용자를 조회하는 로직이라고 가정합니다.
    // 실제로는 사용자를 데이터베이스나 다른 저장소에서 조회하도록 구현해야 합니다.
    public User findUserById(String userId) {
        // 사용자 조회 로직을 구현하세요.
        // 예시로 더미 데이터를 사용하겠습니다.
        if ("사용자 pk".equals(userId)) {
            //return new User("사용자 pk", "name", false, 10000);
        }
        return null;
    }

    public String getUserIdFromToken(String token) {
        return jwtProvider.validateTokenAndGetSubject(token);
    }
}
