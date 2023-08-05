package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.nanum.domain.product.Product;
import server.nanum.domain.Seller;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value="SELECT p FROM Product p WHERE p.seller= :seller ORDER BY p.createAt DESC")
    List<Product> findAllBySellerOrderByCreateAt(@Param("seller")Seller seller);
    boolean existsByName(@Param("seller")String name);
}
