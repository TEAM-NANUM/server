package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.domain.User;
import server.nanum.dto.user.UserResponseDTO;
import server.nanum.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("userDetails.getUsername() = {}", userDetails.getUsername());
        log.info("userDetails.getAuthorities() = {}", userDetails.getAuthorities());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("@@@: " + authentication.getPrincipal().toString());
//        User user = (User)authentication.getPrincipal();
//
//        log.info(user.getPassword());
//
//        UserResponseDTO responseDTO = UserResponseDTO.builder()
//                .userId(String.valueOf(user.getId()))
//                .name(user.getName())
//                .isGuest(false)
//                .point(user.getPoint())
//                .build();

        return ResponseEntity.ok().build();

    }
}
