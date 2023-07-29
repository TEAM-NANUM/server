package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
