package server.nanum.dto.user.response;

import org.springframework.stereotype.Service;
import server.nanum.domain.Seller;
import server.nanum.domain.User;

import static server.nanum.dto.user.response.LoginResponseDTO.UserResponseDTO;

@Service
public class LoginResponseFactory {
    /**
     * User 객체와 JWT 토큰을 사용하여 LoginResponseDTO를 생성하는 팩토리 메소드
     * @param user 사용자 객체
     * @param token JWT 토큰
     * @return LoginResponseDTO 인스턴스
     */
    public LoginResponseDTO createLoginResponseDTO(User user, String token) {
        UserResponseDTO userResponseDTO = createUserResponseDTO(user);
        return createLoginResponseDTO(token, userResponseDTO);
    }

    /**
     * Seller 객체와 JWT 토큰을 사용하여 LoginResponseDTO를 생성하는 팩토리 메소드
     * @param seller 판매자 객체
     * @param token JWT 토큰
     * @return LoginResponseDTO 인스턴스
     */
    public LoginResponseDTO createLoginResponseDTO(Seller seller, String token) {
        UserResponseDTO userResponseDTO = createUserResponseDTO(seller);
        return createLoginResponseDTO(token, userResponseDTO);
    }

    private UserResponseDTO createUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getName())
                .role(String.valueOf(user.getUserRole()))
                .build();
    }

    private UserResponseDTO createUserResponseDTO(Seller seller) {
        return UserResponseDTO.builder()
                .id(String.valueOf(seller.getId()))
                .username(seller.getName())
                .role("SELLER")
                .build();
    }

    private LoginResponseDTO createLoginResponseDTO(String token, UserResponseDTO userResponseDTO) {
        return LoginResponseDTO.builder()
                .token(token)
                .userResponseDTO(userResponseDTO)
                .build();
    }
}


