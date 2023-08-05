package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.Delivery;

public interface DeliveryRepository  extends JpaRepository<Delivery,Long> {
}
