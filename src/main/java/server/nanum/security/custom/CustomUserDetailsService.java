package server.nanum.security.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.repository.SellerRepository;
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
    private final SellerRepository sellerRepository;

    /**
     * GUEST 역할을 나타내는 상수입니다.
     */
    private static final String GUEST_ROLE = "GUEST";

    /**
     * HOST 역할을 나타내는 상수입니다.
     */
    private static final String HOST_ROLE = "HOST";

    /**
     * SELLER 역할을 나타내는 상수입니다.
     */
    private static final String SELLER_ROLE = "SELLER";

    @Override
    public UserDetails loadUserByUsername(String userInfo) throws UsernameNotFoundException {
        String[] idAndRoleArray = userInfo.split(":");
        validateUserInfo(idAndRoleArray);

        String id = idAndRoleArray[0];
        String role = idAndRoleArray[1];

        return switch (role) {
            case GUEST_ROLE, HOST_ROLE -> loadUserById(id);
            case SELLER_ROLE -> loadSellerById(id);
            default -> throw new JwtAuthenticationException("지원하지 않는 사용자입니다!");
        };
    }

    /**
     * 사용자 ID를 이용하여 사용자 정보를 로드합니다.
     *
     * @param id 사용자 ID
     * @return UserDetails 사용자 정보
     * @throws JwtAuthenticationException 해당 토큰과 맞는 사용자가 존재하지 않을 때 예외 발생
     */
    private UserDetails loadUserById(String id) {
        return userRepository.findById(Long.valueOf(id))
                .map(UserWrapper::new)
                .orElseThrow(() -> new JwtAuthenticationException("해당 토큰과 맞는 사용자가 존재하지 않습니다!"));
    }

    /**
     * 판매자 ID를 이용하여 판매자 정보를 로드합니다.
     *
     * @param id 판매자 ID
     * @return UserDetails 판매자 정보
     * @throws JwtAuthenticationException 해당 토큰과 맞는 판매자가 존재하지 않을 때 예외 발생
     */
    private UserDetails loadSellerById(String id) {
        return sellerRepository.findById(Long.valueOf(id))
                .map(SellerWrapper::new)
                .orElseThrow(() -> new JwtAuthenticationException("해당 토큰과 맞는 판매자가 존재하지 않습니다!"));
    }

    /**
     * 유효한 사용자 정보인지 확인합니다.
     *
     * @param idAndRoleArray 사용자 정보 배열 [0]: ID, [1]: 역할
     * @throws JwtAuthenticationException 페이로드가 유효하지 않을 때 예외 발생
     */
    private static void validateUserInfo(String[] idAndRoleArray) {
        if (idAndRoleArray.length != 2 || idAndRoleArray[0].isEmpty() || idAndRoleArray[1].isEmpty()) {
            throw new JwtAuthenticationException("페이로드가 유효하지 않습니다!");
        }
    }
}





