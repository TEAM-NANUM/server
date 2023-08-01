package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Seller;
@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
}
