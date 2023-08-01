package server.nanum.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUid(Long uid);


    @EntityGraph(attributePaths = {"userGroup"})
    Optional<User> findById(Long id);
}
