package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.nanum.domain.*;
import server.nanum.domain.product.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT o from Order o LEFT JOIN o.review r WHERE o.user = :user AND r IS NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") DeliveryStatus delivery);
    @Query(value = "SELECT o from Order o LEFT JOIN o.review r WHERE o.user = :user AND r IS NOT NULL AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") DeliveryStatus delivery);
    @Query(value = "SELECT o from Order o WHERE o.user = :user AND o.deliveryStatus= :delivery ORDER BY o.createAt DESC")
    List<Order> findByUserAndDeliveryStatusOrderByCreateAtDesc(@Param("user") User user, @Param("delivery") DeliveryStatus delivery);

    @Query(value = "SELECT o from Order o WHERE o.product = :product ORDER BY o.createAt DESC")
    List<Order> findByProductOrderByCreateAtDesc(@Param("product") Product product);
    @Query(value = "SELECT SUM(COALESCE(o.review.rating, 0)) FROM Order o WHERE o.product= :product")
    Float  calculateTotalRatingSum(@Param("product") Product product);
    @Query("SELECT COUNT(o) FROM Order o LEFT JOIN o.review r WHERE r IS NOT NULL AND o.product= :product")
    long countByReviewIsNotNull(@Param("product") Product product);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryStatus = :delivery")
    long countByDeliveryStatus(@Param("delivery") DeliveryStatus delivery);
    @Query(value = "SELECT o from Order o WHERE o.user.userGroup = :userGroup AND o.deliveryStatus= :delivery ORDER BY o.user.name ASC, o.createAt DESC")
    List<Order> findByUserUserGroupAndDeliveryStatusOrdered(@Param("userGroup") UserGroup userGroup, @Param("delivery") DeliveryStatus delivery);
}
