package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.*;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.response.AllReviewsDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.exception.BadRequestException;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
            .ratingAvg(0.0F)
            .reviewCnt(0)
            .subCategory(subCategory)
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
    User user2 = User.builder()
            .name("test")
            .id(2L)
            .uid(200L)
            .userRole(UserRole.HOST)
            .userGroup(userGroup)
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
    Order order4 = Order.builder()
            .id(4L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(2)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .user(user)
            .totalAmount(2000)
            .build();
    Order order6 = Order.builder()
            .id(6L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(6)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .user(user)
            .totalAmount(6000)
            .build();
    Review review1 = Review.builder()
            .comment("맛있음")
            .rating(4.5F)
            .id(1L)
            .order(order3)
            .createAt(LocalDateTime.MIN)
            .build();
    Review review2 = Review.builder()
            .comment("약간 상함")
            .rating(4.1F)
            .id(2L)
            .order(order2)
            .createAt(LocalDateTime.now().minusDays(3))
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
        orderList.add(order4);
//        when(orderRepository.calculateTotalRatingSum(product)).thenReturn(5.0F);
//        when(orderRepository.countByReviewIsNotNull(product)).thenReturn(1L);
        Review reviewTest = dto.toEntity(order3);
        reviewService.createReview(dto,user);


        AddReviewDTO dto2 = new AddReviewDTO(5L,5.0F,"맛있어요");
        assertThrows(NotFoundException.class,()-> reviewService.createReview(dto2,user));

        AddReviewDTO dto3 = new AddReviewDTO(4L,1.0F,"맛있어요");
        when(orderRepository.findById(4L)).thenReturn(Optional.of(order4));
//        when(orderRepository.calculateTotalRatingSum(product)).thenReturn(6.0F);
//        when(orderRepository.countByReviewIsNotNull(product)).thenReturn(2L);
        when(orderRepository.findById(6L)).thenReturn(Optional.of(order6));
        AddReviewDTO dto4 = new AddReviewDTO(6L,1.0F,"맛있어요");
        assertThrows(BadRequestException.class,()->reviewService.createReview(dto4,user2));
        Review reviewTest2 = dto3.toEntity(order4);
        reviewService.createReview(dto3,user);

        assertAll(
                ()->assertEquals(5.0F,reviewTest.getRating(),()->"5여야함"),
                ()->assertEquals(3,reviewTest.getOrder().getProductCount(),()->"3여야함"),
                ()->assertEquals("토마토",reviewTest.getOrder().getProduct().getName(),()->"이름이 토마토임"),
                ()->assertEquals(3.0F,reviewTest2.getOrder().getProduct().getRatingAvg(),()->"평균평점 5.0점"),
                ()->assertEquals(5.0F,order3.getReview().getRating(),()-> "주문 -> 리뷰 별점 확인"),
                ()->assertEquals(1.0F,reviewTest2.getRating(),()->"0여야함")
        );
    }

    @Test
    @DisplayName("리뷰없는 주문 조회 테스트")
    @org.junit.jupiter.api.Order(2)
    public void testGetUnReviewOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        when(orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED)).thenReturn(orders);
        MyUnReviewOrdersDTO result = reviewService.getUnReviewOrder(user);
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
        when(orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.DELIVERED)).thenReturn(orders);
        MyReviewOrdersDTO result = reviewService.getReviewedOrder(user);
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

        assertThrows(NotFoundException.class,()->reviewService.getProductReviews(5L));
        
        assertAll(
                ()->assertEquals(2,result.getReviews().size(),()->"2개여야함"),
                ()->assertEquals(1L,result.getReviews().get(0).getId(),()->"올바른 리뷰가 나와야함")
        );
    }

    @Test
    @DisplayName("리뷰 개수제한 조회")
    @org.junit.jupiter.api.Order(4)
    public void testAllReview() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        Review review3 = Review.builder()
                .comment("약간 상함333")
                .rating(4.1F)
                .id(3L)
                .order(order3)
                .createAt(LocalDateTime.now())
                .build();
        reviews.add(review3);
        when(reviewRepository.findAllByOrderByCreateAtDesc()).thenReturn(reviews);
        AllReviewsDTO result = reviewService.getAllReviews(2);
        assertAll(
                ()->assertEquals(2,result.reviews().size(),()->"2개여야함"),
                ()->assertEquals(2,result.count(),()->"2개여야함"),
                ()->assertEquals(1L,result.reviews().get(0).id(),()->"올바른 리뷰가 나와야함")
        );
    }

}
