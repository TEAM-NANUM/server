package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.service.adapter.UserAdapter;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 서비스 클래스
 * 다양한 사용자 어댑터들을 활용하여 판매자, 게스트, 호스트에 대한 로그인 또는 회원 가입을 처리합니다.
 *
 * 사용자 어댑터들은 UserDTO 객체를 받아서 해당 사용자 유형에 맞게 로그인 또는 회원 가입을 수행합니다.
 * 이 클래스는 UserDTO 객체를 사용하여 적절한 사용자 어댑터를 선택하고,
 * 해당 어댑터를 통해 로그인 또는 회원 가입 처리를 수행합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoginService {
    private final List<UserAdapter> userAdapters;

    /**
     * 로그인 또는 회원 가입 처리를 수행합니다.
     *
     * @param userLoginRequestDTO 로그인 또는 회원 가입에 필요한 사용자 정보 객체
     * @return 인증 응답 DTO
     * @throws IllegalArgumentException 지원되지 않는 사용자 유형일 경우 예외를 던집니다.
     */
    public LoginResponseDTO loginOrCreate(UserLoginRequestDTO userLoginRequestDTO) {
        return getUserAdapterFor(userLoginRequestDTO)
                .map(adapter -> adapter.login(userLoginRequestDTO))
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 사용자 유형입니다."));
    }

    private Optional<UserAdapter> getUserAdapterFor(UserLoginRequestDTO userLoginRequestDTO) {
        return userAdapters.stream()
                .filter(adapter -> adapter.supports(userLoginRequestDTO))
                .findFirst();
    }
}




