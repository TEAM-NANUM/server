package server.nanum.repository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.Seller;
import server.nanum.domain.product.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class ProductRepsitoryTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;
    Seller seller1=Seller.builder().name("1").build();
    Seller seller2=Seller.builder().name("2").build();
    @BeforeEach
    public void setUp(){
        sellerRepository.save(seller1);
        sellerRepository.save(seller2);
        Product product1 = Product.builder().seller(seller1).name("1").price(1).build();
        Product product2 = Product.builder().seller(seller2).name("2").price(2).build();
        Product product3 = Product.builder().seller(seller1).name("3").price(3).build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
    @Test
    @Order(1)
    @DisplayName("판매자로 상품 찾기")
    public void findBySeller(){
        List<Product> productList = productRepository.findAllBySellerOrderByCreateAt(seller1);
        assertAll(
                ()->assertEquals(2,productList.size(),()->"개수 다름"),
                ()->assertEquals(1,productList.get(1).getPrice(),()->"상품 다름"),
                ()->assertEquals(3,productList.get(0).getPrice(),()->"상품 다름"),
                ()->assertEquals("1",productList.get(0).getSeller().getName(),()->"상품 다름")
        );
    }
    @Test
    @Order(2)
    @DisplayName("이름 여부 확인")
    public void checkName(){
        assertAll(
                ()->assertTrue(productRepository.existsByName("1"),()->"이름 없다함"),
                ()->assertTrue(productRepository.existsByName("2"),()->"이름 없다함"),
                ()->assertTrue(productRepository.existsByName("3"),()->"이름 없다함"),
                ()->assertFalse(productRepository.existsByName("111111"),()->"이름 있다함")
        );
    }
}
