package server.nanum.repository;

import jakarta.transaction.Transactional;
import org.h2.mvstore.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.*;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddressDTO;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class ReviewRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    Product product = Product.builder()
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
    Address address = Address.builder()
            .zipCode("100")
            .defaultAddress("00동")
            .detailAddress("001호")
            .build();

    AddressDTO dto = AddressDTO.builder()
            .zipCode(address.getZipCode())
            .defaultAddress(address.getDefaultAddress())
            .detailAddress(address.getDetailAddress())
            .build();
    UserGroup userGroup = UserGroup.builder()
            .point(10000)
            .build();
    User user = User.builder()
            .name("test")
            .uid(100L)
            .userRole(UserRole.HOST)
            .userGroup(userGroup)
            .build();



    @BeforeEach
    public void setUp(){
        productRepository.save(product);
        productRepository.save(product2);
        userGroupRepository.save(userGroup);
        userRepository.save(user);
        Order order1 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product)
                .productCount(1)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETE)
                .user(user)
                .totalAmount(1000)
                .build();
        Order order2 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product)
                .productCount(2)
                .deliveryStatus(DeliveryStatus.IN_PROGRESS)
                .user(user)
                .totalAmount(2000)
                .build();
        Order order3 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product)
                .productCount(3)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .user(user)
                .totalAmount(3000)
                .build();
        Order order4 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product)
                .productCount(4)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .user(user)
                .totalAmount(4000)
                .build();
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        Review review1 = Review.builder()
                .comment("맛있음")
                .rating(4.5F)
                .order(order3)
                .build();
        Review review2 = Review.builder()
                .comment("약간 상함")
                .rating(4.1F)
                .order(order4)
                .build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        Order order5 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product2)
                .productCount(5)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .user(user)
                .totalAmount(5000)
                .build();
        Order order6 = Order.builder()
                .deliveryAddress(dto.toString())
                .product(product2)
                .productCount(6)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .user(user)
                .totalAmount(6000)
                .build();
        Review review3 = Review.builder()
                .comment("3번후기")
                .rating(2.5F)
                .order(order5)
                .build();
        Review review4 = Review.builder()
                .comment("4번후기")
                .rating(3.5F)
                .order(order6)
                .build();
        orderRepository.save(order5);
        orderRepository.save(order6);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
    }
    @Test
    @DisplayName("제품 id로 리뷰 조회 테스트")
    @org.junit.jupiter.api.Order(1)
    public void productIdFind(){
        List<Review> reviewList = reviewRepository.findAllByOrderProductId(product.getId());
        assertAll(
                ()->assertEquals(2,reviewList.size(),()->"2개 아님"),
                ()->assertEquals(4.5F,reviewList.get(0).getRating(),()->"주문 틀림"),
                ()->assertEquals(4.1F,reviewList.get(1).getRating(),()->"주문 틀림")
        );
    }
    @Test
    @DisplayName("상위 리뷰 가져오기 조회")
    @org.junit.jupiter.api.Order(2)
    public void topLimitFind(){
        int n=3;
        List<Review> reviewList = reviewRepository.findAllByOrderByCreateAtDesc();
        assertAll(
                ()->assertEquals(4,reviewList.size(),()->"3개 아님"),
                ()->assertEquals(3.5F,reviewList.get(0).getRating(),()->"주문 틀림"),
                ()->assertEquals(2.5F,reviewList.get(1).getRating(),()->"주문 틀림"),
                ()->assertEquals(4.1F,reviewList.get(2).getRating(),()->"주문 틀림")
        );
    }
}