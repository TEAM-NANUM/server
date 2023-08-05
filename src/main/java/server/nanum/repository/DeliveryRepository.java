package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;

import java.util.Optional;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    @Query(value="SELECT d FROM Delivery d WHERE d.user = :user AND d.isDefault = true")
    Optional<Delivery> findByUserAndIsDefaultTrue(@Param("user") User user);
}
