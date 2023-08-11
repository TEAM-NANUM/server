package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.domain.product.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.review IS NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") String delivery);
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.review IS NOT NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") String delivery);
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") String delivery);

    @Query(value = "SELECT o from Order o WHERE o.product = :product ORDER BY o.createAt DESC")
    List<Order> findByProductOrderByCreateAtDesc(@Param("product") Product product);

    @Query(value = "SELECT o from Order o WHERE o.user.userGroup = :userGroup AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserUserGroupAndDeliveryStatusOrderByCreateAtDesc(@Param("userGroup") UserGroup userGroup, @Param("delivery") String delivery);
}
