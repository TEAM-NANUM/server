package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.dto.user.response.GuestDTO;
import server.nanum.service.SignupService;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    /**
     * 게스트용 회원가입 API
     *
     * @param guestDTO 게스트 회원가입 요청 DTO
     * @return ResponseEntity<LoginResponseDTO> 회원가입 결과와 인증 응답 DTO
     */
    @PostMapping("/guest")
    public ResponseEntity<Void> registerGuestUser(@RequestBody GuestDTO guestDTO) {
        // 로그인 또는 회원가입 처리
        signupService.registerGuestUser(guestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
