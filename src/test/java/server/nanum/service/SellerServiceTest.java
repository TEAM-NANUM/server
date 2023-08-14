package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.*;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddProductDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.response.SellerInfoDTO;
import server.nanum.dto.response.SellerOrdersDTO;
import server.nanum.dto.response.SellerProductsDTO;
import server.nanum.exception.ConflictException;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class SellerServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @InjectMocks
    private SellerService sellerService;

    Category category = Category.builder()
            .name("농산물")
            .id(1L)
            .build();
    SubCategory subCategory = SubCategory.builder()
            .category(category)
            .id(1L)
            .name("쌀")
            .build();
    Address address = Address.builder()
            .zipCode("100")
            .defaultAddress("00동")
            .detailAddress("001호")
            .build();
    Seller seller = Seller.builder()
            .name("판매")
            .id(1L)
            .address(address)
            .email("string")
            .password("1234")
            .phoneNumber("1234")
            .build();
    Seller seller2 = Seller.builder()
            .name("판매")
            .id(2L)
            .address(address)
            .email("string")
            .password("1234")
            .phoneNumber("1234")
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
            .seller(seller)
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

    @BeforeEach
    public void setup() {

    }

    @Test
    @DisplayName("상품 생성 테스트")
    @org.junit.jupiter.api.Order(1)
    public void testCreateProduct() {
        AddProductDTO dto = new AddProductDTO("쌀",100,2,"유기농","string",DeliveryType.PACKAGE,1L);
        when(subCategoryRepository.findById(1L)).thenReturn(Optional.of(subCategory));
        when(productRepository.existsByName("쌀")).thenReturn(false);
        when(productRepository.existsByName("망고")).thenReturn(true);
        Product productTest = dto.toEntity(seller,subCategory);
        sellerService.createProduct(seller, dto);

        AddProductDTO dto2 = new AddProductDTO("망고",100,2,"유기농","string",DeliveryType.PACKAGE,1L);
        AddProductDTO dto3 = new AddProductDTO("쌀",100,2,"유기농","string",DeliveryType.PACKAGE,2L);
        assertThrows(ConflictException.class,()->sellerService.createProduct(seller,dto2));
        assertThrows(NotFoundException.class,()->sellerService.createProduct(seller,dto3));

        assertAll(
                ()->assertEquals("쌀",productTest.getSubCategory().getName(),()->"카테고리가 올바르지않음"),
                ()->assertEquals("쌀",productTest.getName(),()->"이름이 쌀이여야함"),
                ()->assertEquals("판매",productTest.getSeller().getName(),()->"판매자 이름이 올바름")
        );

    }

    @Test
    @DisplayName("판매자 정보 조회 테스트")
    @org.junit.jupiter.api.Order(2)
    public void testGetSellerInfo() {
        SellerInfoDTO result = sellerService.getSellerInfo(seller);

        assertAll(
                ()->assertEquals("판매",result.userName())
        );
    }

    @Test
    @DisplayName("판매자 상품 조회 테스트")
    @org.junit.jupiter.api.Order(3)
    public void testGetSellerProducts() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(productRepository.findAllBySellerOrderByCreateAt(seller)).thenReturn(products);
        SellerProductsDTO result = sellerService.getSellerProducts(seller);

        assertAll(
                ()->assertEquals(1L,result.products().get(0).productId(),()->"올바른 상품임"),
                ()->assertEquals("토마토",result.products().get(0).name(),()->"이름이 토마토임"),
                ()->assertEquals(1,result.products().size(),()->"개수가 1개임")
        );
    }

    @Test
    @DisplayName("판매자 상품 주문 조회 테스트")
    @org.junit.jupiter.api.Order(4)
    public void testGetSellerOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        Long productId=1L;
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.findByProductOrderByCreateAtDesc(product)).thenReturn(orders);
        SellerOrdersDTO result = sellerService.getSellerOrders(productId,seller);

        assertThrows(NotFoundException.class,()->sellerService.getSellerOrders(2L,seller2));

        assertAll(
                ()->assertEquals(3,result.orders().size(),()->"3개여야함"),
                ()->assertEquals(1L,result.orders().get(0).id(),()->"올바른 주문이 나와야함"),
                ()->assertEquals(2L,result.orders().get(1).id(),()->"올바른 주문이 나와야함"),
                ()->assertEquals(3L,result.orders().get(2).id(),()->"올바른 주문이 나와야함")
        );
    }
}
