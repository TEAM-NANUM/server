package server.nanum.service.user.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.dto.user.AuthResponseDTO;
import server.nanum.domain.dto.user.GuestUserDTO;
import server.nanum.repository.UserRepository;
import server.nanum.domain.dto.user.UserDTO;

//@Service
//@RequiredArgsConstructor
//@Transactional
//@Slf4j
//public class GuestUserAdapter implements UserAdapter {
//    private final UserRepository userRepository;
//
//    @Override
//    public boolean supports(UserDTO userDTO) {
//        // 사용자 DTO가 게스트 사용자를 나타내는지 확인하는 조건을 구현해야 합니다.
//        // 예를 들어, 특정 필드나 타입을 확인하는 방식으로 할 수 있어요.
//        // 이 예제에서는 GuestUserDTO가 게스트 사용자를 나타내는 것으로 가정합니다.
//        return userDTO instanceof GuestUserDTO;
//    }
//
//    @Override
//    public AuthResponseDTO loginOrRegister(UserDTO userDTO) {
//        return AuthResponseDTO;
//    }
//}

