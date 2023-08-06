package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Cart;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByIdAndUser(Long id, User user);
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndProduct(User user, Product product);
    List<Cart> findByUserAndIdIn(User user, List<Long> cartIds);
}
