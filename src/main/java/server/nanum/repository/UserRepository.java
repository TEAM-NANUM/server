package server.nanum.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.domain.UserRole;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUid(Long uid);

    @EntityGraph(attributePaths = {"userGroup"})
    Optional<User> findById(Long id);

    Optional<User> findByInviteCode(String inviteCode);

    @Query("SELECT u FROM User u WHERE u.userGroup= :userGroup AND u.userRole= :role")
    Optional<User> findByUserGroupAndUserRole(@Param("userGroup") UserGroup userGroup,@Param("role") UserRole role);
}
