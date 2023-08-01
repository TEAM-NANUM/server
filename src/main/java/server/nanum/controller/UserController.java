package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.user.HostResponseDTO;
import server.nanum.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String testSecurity(@CurrentUser User user) {
        log.info("컨텍스트 홀더 안에 객체 = {}" , SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .getClass()
                .getName()
        );
        return "로그인 성공";
    }

    @GetMapping
    public ResponseEntity<HostResponseDTO> getUserInfo(@CurrentUser User user) {

        HostResponseDTO responseDTO = userService.getUserInfo(user);

        // 리턴할 때 생성한 DTO를 사용
        return ResponseEntity.ok(responseDTO);
    }
}
