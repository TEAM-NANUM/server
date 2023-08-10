package server.nanum.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.*;
import server.nanum.domain.Order;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReviewServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
    }
    Category category = Category.builder()
            .name("농산물")
            .id(1L)
            .build();
    SubCategory subCategory = SubCategory.builder()
            .category(category)
            .id(1L)
            .name("쌀")
            .build();
    Product product = Product.builder()
            .name("토마토")
            .id(1L)
            .price(1000)
            .unit(10)
            .deliveryType(DeliveryType.PACKAGE)
            .imgUrl("String")
            .purchaseCnt(0)
            .ratingAvg(5.0F)
            .reviewCnt(5)
            .subCategory(subCategory)
            .build();
    Address address = Address.builder()
            .zipCode("100")
            .defaultAddress("00동")
            .detailAddress("001호")
            .build();
    Seller seller = Seller.builder()
            .name("판매")
            .address(address)
            .email("string")
            .password("qwer")
            .phoneNumber("1234")
            .build();

    AddressDTO dto = AddressDTO.builder()
            .zipCode(address.getZipCode())
            .defaultAddress(address.getDefaultAddress())
            .detailAddress(address.getDetailAddress())
            .build();
    UserGroup userGroup = UserGroup.builder()
            .id(1L)
            .point(10000)
            .build();
    User user = User.builder()
            .name("test")
            .id(1L)
            .uid(100L)
            .userRole(UserRole.HOST)
            .userGroup(userGroup)
            .build();
    Delivery delivery = Delivery.builder()
            .id(1L)
            .isDefault(true)
            .user(user)
            .nickname("본가")
            .address(address)
            .phoneNumber("010-1234-5678")
            .build();
    Order order1 = Order.builder()
            .id(1L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(1)
            .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETE)
            .user(user)
            .totalAmount(1000)
            .build();
    Order order2 = Order.builder()
            .id(2L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(2)
            .deliveryStatus(DeliveryStatus.IN_PROGRESS)
            .user(user)
            .totalAmount(2000)
            .build();
    Order order3 = Order.builder()
            .id(3L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(3)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .user(user)
            .totalAmount(3000)
            .build();
    Review review1 = Review.builder()
            .comment("맛있음")
            .rating(4.5F)
            .id(1L)
            .order(order3)
            .build();
    Review review2 = Review.builder()
            .comment("약간 상함")
            .rating(4.1F)
            .id(2L)
            .order(order2)
            .build();
    @Test
    @DisplayName("리뷰생성 테스트")
    @org.junit.jupiter.api.Order(1)
    public void testCreateReview() {
        AddReviewDTO dto = new AddReviewDTO(3L,5.0F,"맛있어요");
        when(orderRepository.findById(3L)).thenReturn(Optional.of(order3));
        List<Order> orderList = new ArrayList<>();
        orderList.add(order3);
        orderList.add(order2);
        orderList.add(order1);
        when(orderRepository.findByProductOrderByCreateAtDesc(product)).thenReturn(orderList);
        order3.getProduct().getRatingAvg();
        Review reviewTest = dto.toEntity(order3);
        reviewService.createReview(dto);
        assertAll(
                ()->assertEquals(5.0F,reviewTest.getRating(),()->"5여야함"),
                ()->assertEquals(3,reviewTest.getOrder().getProductCount(),()->"3여야함"),
                ()->assertEquals("토마토",reviewTest.getOrder().getProduct().getName(),()->"이름이 토마토임"),
                ()->assertEquals(5.0F,reviewTest.getOrder().getProduct().getRatingAvg(),()->"평균평점 5.0점")
        );
    }

    @Test
    @DisplayName("리뷰없는 주문 조회 테스트")
    @org.junit.jupiter.api.Order(2)
    public void testGetUnReviewOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        when(orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED.toString())).thenReturn(orders);
        MyUnReviewOrdersDTO result = reviewService.GetUnReviewOrder(user);
        assertAll(
                ()->assertEquals(2,result.orders().size(),()->"2개여야함"),
                ()->assertEquals(1L,result.orders().get(0).id(),()->"올바른 주문이 나와야함"),
                ()->assertEquals(2L,result.orders().get(1).id(),()->"올바른 주문이 나와야함")
        );

    }

    @Test
    @DisplayName("리뷰있는 주문 조회 테스트")
    @org.junit.jupiter.api.Order(3)
    public void testGetReviewedOrder() {
        order3.setReview(review1);
        List<Order> orders = new ArrayList<>();
        orders.add(order3);
        when(orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED.toString())).thenReturn(orders);
        MyReviewOrdersDTO result = reviewService.GetReviewedOrder(user);
        assertAll(
                ()->assertEquals(1,result.orders().size(),()->"1개여야함"),
                ()->assertEquals(3L,result.orders().get(0).id(),()->"올바른 주문이 나와야함")
        );
    }

    @Test
    @DisplayName("제품에 있는 모든 리뷰 조회 테스트")
    @org.junit.jupiter.api.Order(4)
    public void testGetProductReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        Long productId=product.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.findAllByOrderProductId(productId)).thenReturn(reviews);
        ProductReviewDTO.ReviewList result = reviewService.getProductReviews(productId);
        assertAll(
                ()->assertEquals(2,result.getReviews().size(),()->"2개여야함"),
                ()->assertEquals(1L,result.getReviews().get(0).getId(),()->"올바른 리뷰가 나와야함")
        );
    }

}