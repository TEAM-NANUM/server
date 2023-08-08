package server.nanum.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;

import java.util.List;


public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("SELECT u FROM User u JOIN u.userGroup ug WHERE ug.id = :userGroupId")
    List<User> findUsersByUserGroupId(@Param("userGroupId") Long userGroupId);
}

