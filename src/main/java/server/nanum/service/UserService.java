package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.dto.user.response.HostResponseDTO;
import server.nanum.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Transactional(readOnly = true)
    public HostResponseDTO getUserInfo(User user) {

        return HostResponseDTO.toDTO(user);
    }
}