package server.nanum.repository;

import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.*;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddressDTO;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
//@SpringBootTest
@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class OrderRepositoryTest {
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
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("유저와 리뷰 없는 주문 찾기")
    public void findReviewNull(){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.IN_PROGRESS);
        assertAll(
                ()-> assertEquals(1,orderList.size(),()->"사이즈 1여야함"),
                ()-> assertEquals(2,orderList.get(0).getProductCount(),()->"2번주문이여야함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("유저와 리뷰 있는 주문 찾기")
    public void findReviewExist(){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED);
        assertAll(
                ()-> assertEquals(2,orderList.size(),()->"사이즈 2여야함"),
                ()-> assertEquals(4,orderList.get(0).getProductCount(),()->"4번주문이여야함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("유저, 주문상태 조회")
    public void findDelivery(){
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED) ;
        assertAll(
                ()-> assertEquals(2,orderList.size(),()->"사이즈 2여야함"),
                ()-> assertEquals(4,orderList.get(0).getProductCount(),()->"4번주문이여야함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("상품 조회")
    public void findProduct(){
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product);
        assertAll(
                ()-> assertEquals(4,orderList.size(),()->"사이즈 4여야함"),
                ()-> assertEquals(3,orderList.get(1).getProductCount(),()->"3번주문이여야함")
        );
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("총합조회")
    public void sumRating(){
        Float sum = orderRepository.calculateTotalRatingSum(product);
        assertAll(
                ()-> assertEquals(8.6F,sum,()->"평균 이상함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("개수조회")
    public void countReview(){
        long count = orderRepository.countByReviewIsNotNull(product);
        assertAll(
                ()-> assertEquals(2,count,()->"개수 이상함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("주문 상태 개수조회")
    public void countDelivery(){
        long count1 = orderRepository.countByDeliveryStatus(DeliveryStatus.DELIVERED);
        long count2 = orderRepository.countByDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        long count3 = orderRepository.countByDeliveryStatus(DeliveryStatus.PAYMENT_COMPLETE);
        assertAll(
                ()-> assertEquals(2,count1,()->"개수 이상함"),
                ()-> assertEquals(1,count2,()->"개수 이상함"),
                ()-> assertEquals(1,count3,()->"개수 이상함")
        );
    }
    @Test
    @org.junit.jupiter.api.Order(8)
    @DisplayName("그룹 , 주문 상태 조회")
    public void findGroup(){
        List<Order> orderList = orderRepository.findByUserUserGroupAndDeliveryStatusOrdered(userGroup,DeliveryStatus.DELIVERED);
        assertAll(
                ()-> assertEquals(2,orderList.size(),()->"사이즈 2여야함"),
                ()-> assertEquals(3,orderList.get(1).getProductCount(),()->"3번주문이여야함")
        );
    }
}

