package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.TokenBlacklist;

public interface TokenBlacklistRepository  extends JpaRepository<TokenBlacklist, String> {
}
