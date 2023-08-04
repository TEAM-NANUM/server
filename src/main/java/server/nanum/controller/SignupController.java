package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.service.SignupService;

/**
 * 회원가입 컨트롤러
 *
 * @author hyunjin
 * @version 1.0.0
 * @date 2023-08-05
 */
@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    /**
     * 게스트용 회원가입 API
     *
     * @param user 현재 사용자 정보
     * @param guestSignupDTO 게스트 회원가입 요청 DTO
     * @return ResponseEntity<Void> 회원가입 결과 응답
     */
    @PostMapping("/guest")
    public ResponseEntity<Void> registerGuest(@CurrentUser User user, @RequestBody GuestSignupDTO guestSignupDTO) {

        signupService.registerGuest(user, guestSignupDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/seller")
    public ResponseEntity<Void> registerSeller(@RequestBody SellerSignupDTO sellerSignupDTO) {

        signupService.registerSeller(sellerSignupDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}