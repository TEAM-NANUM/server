package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;
import server.nanum.domain.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.review IS NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") String delivery);
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.review IS NOT NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") String delivery);

    @Query(value = "SELECT o from Order o WHERE o.product = :product ORDER BY o.createAt DESC")
    List<Order> findByProductOrderByCreateAtDesc(@Param("product") Product product);
}
