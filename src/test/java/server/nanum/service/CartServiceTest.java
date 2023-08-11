package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import server.nanum.domain.Cart;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.CartRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    private User testUser;
    private Product testProduct1;
    private Product testProduct2;


    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).name("user1").build();
        testProduct1 = Product.builder().id(1L).price(10000).name("product1").build();
        testProduct2 = Product.builder().id(2L).price(10000).name("product2").build();
    }

    @Test
    @DisplayName("유저의 장바구니 항목을 가져올 수 있다.")
    void testGetCartItems() {
        List<Cart> cartItems = new ArrayList<>();
        cartItems.add(Cart.builder()
                .id(1L)
                .product(testProduct1)
                .user(testUser)
                .productCount(2).build());

        when(cartRepository.findByUser(testUser)).thenReturn(cartItems);

        CartResponseDTO.CartList result = cartService.getCartItems(testUser);

        assertAll(
                () -> assertEquals(1, result.getItems().size(),()-> "반환된 장바구니 아이템 총 개수는 1개 여야 한다."),
                () -> assertEquals(2, result.getItems().get(0).getQuantity(),()-> "반환된 장바구니 아이템 하나의 quantity는 2여야 한다."),
                () -> assertEquals(2*10000, result.getItems().get(0).getTotalPrice(),()-> "반환된 장바구니 아이템 하나의 총 금액은 20000 이어야 한다.")
        );
    }

    @Test
    @DisplayName("상품을 장바구니에 추가할 수 있다.")
    void testAddToCart() {
        CartRequestDTO.CartItem cartItem = new CartRequestDTO.CartItem();
        cartItem.setProductId(1L);
        cartItem.setQuantity(3);

        when(productRepository.findById(cartItem.getProductId())).thenReturn(Optional.of(testProduct1));
        when(cartRepository.findByUserAndProduct(testUser, testProduct1)).thenReturn(Optional.empty());

        cartService.addToCart(cartItem, testUser);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 항목의 수량을 업데이트할 수 있다.")
    void testUpdateCartItemQuantity() {
        CartRequestDTO.CartItemQuantity cartItemQuantity = new CartRequestDTO.CartItemQuantity();
        cartItemQuantity.setId(1L);
        cartItemQuantity.setQuantity(5);

        Cart existingCartItem = Cart.builder()
                .id(1L)
                .product(testProduct1)
                .user(testUser)
                .productCount(2).build();

        when(cartRepository.findByIdAndUser(cartItemQuantity.getId(), testUser)).thenReturn(Optional.of(existingCartItem));

        CartResponseDTO.CartListItem result = cartService.updateCartItemQuantity(cartItemQuantity, testUser);

        assertEquals(5, result.getQuantity(), () -> "수정된 수량은 5여야 합니다.");
    }

    @Test
    @DisplayName("장바구니 항목을 삭제할 수 있다.")
    void testRemoveFromCart() {
        CartRequestDTO.CartIdList cartIdList = new CartRequestDTO.CartIdList();
        cartIdList.setItemIds(List.of(1L, 2L));

        List<Cart> cartItems = new ArrayList<>();
        cartItems.add(Cart.builder()
                .id(1L)
                .product(testProduct1)
                .user(testUser)
                .productCount(2).build());
        cartItems.add(Cart.builder()
                .id(2L)
                .product(testProduct2)
                .user(testUser)
                .productCount(2).build());

        when(cartRepository.findByUserAndIdIn(testUser, cartIdList.getItemIds())).thenReturn(cartItems);

        cartService.removeFromCart(cartIdList, testUser);

        verify(cartRepository, times(1)).deleteAllInBatch(cartItems);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 추가하려 할 때 예외가 발생한다.")
    void testAddToCartWithNonExistingProduct() {
        CartRequestDTO.CartItem cartItem = new CartRequestDTO.CartItem();
        cartItem.setProductId(1L);
        cartItem.setQuantity(3);

        when(productRepository.findById(cartItem.getProductId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.addToCart(cartItem, testUser));
    }

    @Test
    @DisplayName("존재하지 않는 장바구니 항목을 업데이트하려 할 때 예외가 발생한다.")
    void testUpdateNonExistingCartItem() {
        CartRequestDTO.CartItemQuantity cartItemQuantity = new CartRequestDTO.CartItemQuantity();
        cartItemQuantity.setId(1L);
        cartItemQuantity.setQuantity(5);

        when(cartRepository.findByIdAndUser(cartItemQuantity.getId(), testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.updateCartItemQuantity(cartItemQuantity, testUser));
    }
}