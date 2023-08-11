package server.nanum.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.*;
import server.nanum.domain.Order;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.response.MyCompleteOrdersDTO;
import server.nanum.dto.response.MyProgressOrdersDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.exception.PaymentRequiredException;
import server.nanum.repository.*;

import java.util.Optional;
import java.util.*;

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
            .build();
    Order order2 = Order.builder()
            .id(2L)
            .deliveryAddress(dto.toString())
            .product(product)
            .productCount(2)
            .deliveryStatus(DeliveryStatus.IN_PROGRESS)
            .user(user)
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
//        orders.add(order1);
        orders.add(order2);
//        orders.add(order3);
        when(orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user,DeliveryStatus.IN_PROGRESS.toString())).thenReturn(orders);
        MyProgressOrdersDTO result = orderService.getInProgressOrder(user);

        assertAll(
                ()->assertEquals(1,result.progressOrders().size(),()->"개수가 1개여야함"),
                ()->assertEquals(2,result.progressOrders().get(0).quantity(),()->"상품 주문 개수는 2개여야함")
        );
    }

    @Test
    @DisplayName("배송완료 주문 조회")
    @org.junit.jupiter.api.Order(4)
    public void testGetCompleteOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(order3);
        when(orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString())).thenReturn(orders);
        MyCompleteOrdersDTO result = orderService.getCompleteOrder(user);

        assertAll(
                ()->assertEquals(1,result.completeOrders().size(),()->"개수가 1개여야함"),
                ()->assertEquals(3,result.completeOrders().get(0).quantity(),()->"상품 주문 개수는 3개여야함")
        );

    }
}
