package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.dto.user.response.HostGetResponseDTO;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Transactional(readOnly = true)
    public HostGetResponseDTO getUserInfo(User user) {

        return HostGetResponseDTO.toDTO(user);
    }
}