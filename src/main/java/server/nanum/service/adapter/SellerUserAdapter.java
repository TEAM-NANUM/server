package server.nanum.service.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Seller;
import server.nanum.dto.user.request.SellerLoginRequestDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.LoginResponseFactory;
import server.nanum.repository.SellerRepository;
import server.nanum.utils.JwtProvider;

/**
 * 판매자 사용자 어댑터 클래스
 * 이메일과 비밀번호를 기반으로 판매자에 대한 로그인을 처리하는 사용자 어댑터입니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SellerUserAdapter implements UserAdapter {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final LoginResponseFactory loginResponseFactory;
    private final PasswordEncoder passwordEncoder;

    /**
     * 판매자 사용자 어댑터가 주어진 사용자 로그인 정보를 지원하는지 여부를 반환합니다.
     *
     * @param userLoginRequestDTO 사용자 로그인 정보
     * @return true(판매자 로그인 정보일 경우) / false(그 외의 경우)
     */
    @Override
    public boolean supports(UserLoginRequestDTO userLoginRequestDTO) {
        return userLoginRequestDTO instanceof SellerLoginRequestDTO;
    }

    /**
     * 판매자 사용자를 로그인 처리합니다.
     * 주어진 이메일로 등록된 판매자가 존재하는지 확인하고, 비밀번호를 검증하여 로그인을 수행합니다.
     *
     * @param userLoginRequestDTO 판매자 사용자의 로그인 정보 (이메일, 비밀번호 포함)
     * @return 인증 응답 DTO
     * @throws IllegalArgumentException 등록되지 않은 이메일 또는 잘못된 비밀번호일 경우 예외를 던집니다.
     */
    @Override
    public LoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        SellerLoginRequestDTO sellerDTO = (SellerLoginRequestDTO) userLoginRequestDTO;

        Seller seller = sellerRepository.findByEmail(sellerDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(sellerDTO.password(), seller.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return createLoginResponse(seller);
    }

    private LoginResponseDTO createLoginResponse(Seller seller) {
        String token = jwtProvider.createToken(String.format("%s:%s", seller.getId(), "SELLER"));
        return loginResponseFactory.createLoginResponseDTO(seller, token);
    }
}
