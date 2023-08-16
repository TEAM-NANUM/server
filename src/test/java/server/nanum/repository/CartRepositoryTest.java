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
import server.nanum.domain.*;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddressDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class CartRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private CartRepository cartRepository;
    Product product1 = Product.builder()
            .name("토마토")
            .price(1000)
            .unit(10)
            .deliveryType(DeliveryType.PACKAGE)
            .imgUrl("String")
            .purchaseCnt(0)
            .ratingAvg(0.0F)
            .reviewCnt(5)
            .build();
    Product product2 = Product.builder()
            .name("감자")
            .price(2000)
            .unit(20)
            .deliveryType(DeliveryType.PACKAGE)
            .imgUrl("String")
            .purchaseCnt(0)
            .ratingAvg(0.0F)
            .reviewCnt(5)
            .build();
    UserGroup userGroup = UserGroup.builder()
            .point(10000)
            .build();
    User user1 = User.builder()
            .name("test1")
            .uid(100L)
            .userRole(UserRole.HOST)
            .userGroup(userGroup)
            .build();
    User user2 = User.builder()
            .name("test2")
            .uid(101L)
            .userRole(UserRole.HOST)
            .userGroup(userGroup)
            .build();
    @BeforeEach
    public void setUp() {
        productRepository.save(product1);
        productRepository.save(product2);
        userGroupRepository.save(userGroup);
        userRepository.save(user1);
        userRepository.save(user2);
        Cart cart1 = Cart.builder()
                .user(user1)
                .productCount(1)
                .product(product1)
                .build();
        Cart cart2 = Cart.builder()
                .user(user1)
                .productCount(2)
                .product(product2)
                .build();
        Cart cart3 = Cart.builder()
                .user(user2)
                .productCount(3)
                .product(product1)
                .build();
        Cart cart4 = Cart.builder()
                .user(user2)
                .productCount(4)
                .product(product2)
                .build();
        cartRepository.save(cart1);
        cartRepository.save(cart2);
        cartRepository.save(cart3);
        cartRepository.save(cart4);
    }
    @Test
    @Order(1)
    @DisplayName("id,user로 조회")
    public void cartIdAndUser(){
        Cart cart1 = Cart.builder().user(user1).productCount(1).product(product1).build();
        Cart cart2 = Cart.builder().user(user1).productCount(2).product(product2).build();
        Cart c1 = cartRepository.save(cart1);
        Cart c2 = cartRepository.save(cart2);
        Cart findCart1 = cartRepository.findByIdAndUser(c1.getId(),user1).get();
        Cart findCart2 = cartRepository.findByIdAndUser(c2.getId(),user1).get();
        assertAll(
                ()->assertEquals(1,findCart1.getProductCount(),()->"다른 카트임"),
                ()->assertEquals(2,findCart2.getProductCount(),()->"다른 카트임")
        );
    }
    @Test
    @Order(2)
    @DisplayName("user로 전체 조회")
    public void cartAllByUser(){
        List<Cart> cartList1 = cartRepository.findByUser(user1);
        List<Cart> cartList2 = cartRepository.findByUser(user2);
        assertAll(
                ()->assertEquals(2,cartList1.size(),()->"개수 다름"),
                ()->assertEquals(2,cartList2.size(),()->"개수 다름"),
                ()->assertEquals(1,cartList1.get(0).getProductCount(),()->"카트 다름"),
                ()->assertEquals(2,cartList1.get(1).getProductCount(),()->"카트 다름"),
                ()->assertEquals(3,cartList2.get(0).getProductCount(),()->"카트 다름"),
                ()->assertEquals(4,cartList2.get(1).getProductCount(),()->"카트 다름")
        );
    }
    @Test
    @Order(3)
    @DisplayName("user,product로 전체 조회")
    public void cartAllByUserAndProduct(){
        Cart cartTest1 = cartRepository.findByUserAndProduct(user1,product1).get();
        Cart cartTest2 = cartRepository.findByUserAndProduct(user2,product2).get();
        assertAll(
                ()->assertEquals(1,cartTest1.getProductCount(),()->"카트 다름"),
                ()->assertEquals(4,cartTest2.getProductCount(),()->"카트 다름")
        );
    }
    @Test
    @Order(4)
    @DisplayName("user,카트 id로 전체 조회")
    public void cartAllByUserAndAllId(){
        Cart cart1 = Cart.builder().user(user1).productCount(1).product(product1).build();
        Cart cart2 = Cart.builder().user(user1).productCount(2).product(product2).build();
        Cart cart3 = Cart.builder().user(user1).productCount(3).product(product1).build();
        Cart cart4 = Cart.builder().user(user2).productCount(4).product(product2).build();
        Cart c1 = cartRepository.save(cart1);
        Cart c2 = cartRepository.save(cart2);
        Cart c3 = cartRepository.save(cart3);
        Cart c4 = cartRepository.save(cart4);
        List<Long> idList = new ArrayList<>();
        idList.add(c1.getId());
        idList.add(c2.getId());
        idList.add(c3.getId());
        idList.add(c4.getId());
        List<Cart> cartList = cartRepository.findByUserAndIdIn(user1,idList);
        assertAll(
                ()->assertEquals(3,cartList.size(),()->"개수 다름"),
                ()->assertEquals(1,cartList.get(0).getProductCount(),()->"카트 다름"),
                ()->assertEquals(2,cartList.get(1).getProductCount(),()->"카트 다름"),
                ()->assertEquals(3,cartList.get(2).getProductCount(),()->"카트 다름")
        );
    }


}
