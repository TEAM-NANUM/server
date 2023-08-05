package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Cart;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findByUser(User user);
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findByUserAndIdIn(User user, List<Long> cartIds);
}
