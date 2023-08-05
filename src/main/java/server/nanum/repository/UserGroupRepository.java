package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.UserGroup;


public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}

