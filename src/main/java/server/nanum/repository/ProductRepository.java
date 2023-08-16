package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Seller;
import server.nanum.domain.product.Product;
import server.nanum.repository.querydsl.ProductRepositoryCustom;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryCustom {
    @Query(value="SELECT p FROM Product p WHERE p.seller= :seller ORDER BY p.createAt DESC")
    List<Product> findAllBySellerOrderByCreateAt(@Param("seller")Seller seller);
    boolean existsByName(@Param("name")String name);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.purchaseCnt = p.purchaseCnt + :count WHERE p IN :products")
    void updatePurchaseCountInBatch(@Param("products") Collection<Product> products, @Param("count") int count);
}
