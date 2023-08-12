package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.*;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.response.MyOrderListDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.exception.PaymentRequiredException;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrderServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryRepository deliveryRepository;


    @InjectMocks
    OrderService orderService;

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
            .createAt(LocalDateTime.now())
            .build();
    Order order2 = Order.builder()
            .id(2L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(2)
            .deliveryStatus(DeliveryStatus.IN_PROGRESS)
            .user(user)
            .createAt(LocalDateTime.MAX)
            .build();
    Order order3 = Order.builder()
            .id(3L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(3)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .user(user)
            .build();
    @BeforeEach
    public void setup() {

    }

    @Test
    @DisplayName("주문생성 테스트")
    @org.junit.jupiter.api.Order(1)
    public void testCreateOrder() {
        AddOrderDTO dto2 = new AddOrderDTO(product.getId(),4,dto);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        orderService.createOrder(dto2,user);
        Order order = dto2.toEntity(product,user);

        AddOrderDTO dto3 = new AddOrderDTO(product.getId(),10000,dto);
        assertThrows(PaymentRequiredException.class,()->orderService.createOrder(dto3,user));

        assertAll(
                ()->assertEquals(4,order.getProductCount(),()->"4여야함"),
                ()->assertEquals("토마토",order.getProduct().getName(),()->"이름이 토마토임"),
                ()->assertEquals("100 00동 001호",order.getDeliveryAddress(),()->"주소가 이상함"),
                ()->assertEquals(6000,order.getUser().getUserGroupPoint(),()->"포인트가 잘못 빠짐")
        );
    }

    @Test
    @DisplayName("유저 정보 테스트")
    @org.junit.jupiter.api.Order(2)
    public void testGetUserDefaultInfo() {
        when(deliveryRepository.findByUserAndIsDefaultTrue(user)).thenReturn(Optional.of(delivery));
        OrderUserInfoDTO result = orderService.getUserDefaultInfo(user);

        User user2 = User.builder()
                .userGroup(userGroup)
                .userRole(UserRole.HOST)
                .name("오류 테스트 유저")
                .uid(1000L)
                .build();
        assertThrows(NotFoundException.class,()->orderService.getUserDefaultInfo(user2));

        assertAll(
                ()->assertEquals("test",result.userName(),()->"유저가 다른 유저임")
        );
    }

    @Test
    @DisplayName("배송진행중 주문 조회")
    @org.junit.jupiter.api.Order(3)
    public void testGetInProgressOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(order2);
        List<Order> orders2 = new ArrayList<>();
        orders.add(order1);
        when(orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.IN_PROGRESS)).thenReturn(orders);
        when(orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.PAYMENT_COMPLETE)).thenReturn(orders2);
        List<DeliveryStatus> deliveryStatusList = new ArrayList<>();
        deliveryStatusList.add(DeliveryStatus.IN_PROGRESS);
        deliveryStatusList.add(DeliveryStatus.PAYMENT_COMPLETE);
        MyOrderListDTO result = orderService.getUserOrder(user,deliveryStatusList);

        assertAll(
                ()->assertEquals(2,result.orderList().size(),()->"개수가 2개여야함"),
                ()->assertEquals(1,result.orderList().get(1).quantity(),()->"상품 주문 개수는 1개여야함"),
                ()->assertEquals(2,result.orderList().get(0).quantity(),()->"상품 주문 개수는 2개여야함")

        );
    }

    @Test
    @DisplayName("배송완료 주문 조회")
    @org.junit.jupiter.api.Order(4)
    public void testGetCompleteOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(order3);

        when(orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED)).thenReturn(orders);
        MyOrderListDTO result = orderService.getUserOrder(user,DeliveryStatus.DELIVERED);

        assertAll(
                ()->assertEquals(1,result.orderList().size(),()->"개수가 1개여야함"),
                ()->assertEquals(3,result.orderList().get(0).quantity(),()->"상품 주문 개수는 3개여야함")
        );

    }
}
