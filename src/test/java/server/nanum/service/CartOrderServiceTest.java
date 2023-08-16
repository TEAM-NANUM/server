package server.nanum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import server.nanum.domain.Cart;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.repository.CartRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CartOrderServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CartRepository cartRepository;

    @Test
    public void CartOrderTest() {
        UserGroup userGroup = UserGroup.createUserGroup(5000);

        User user = User.builder()
                .userGroup(userGroup)
                .build();

        Product product = Product.builder()
                .id(1L)
                .price(1000)
                .purchaseCnt(0)
                .build();

        Cart cart = Cart.createEmptyCartItem(user, product);
        cart.increaseProductCount(3);

        ReflectionTestUtils.setField(cart, "id", 1L);

        List<Long> cartIds = List.of(cart.getId());
        List<Cart> carts = List.of(cart);

        CartRequestDTO.CartIdListAndAddress cartIdListAndAddress = new CartRequestDTO.CartIdListAndAddress();
        cartIdListAndAddress.setItemIds(cartIds);
        cartIdListAndAddress.setAddress(new AddressDTO("111", "222", "333"));

        // cartRepository.findByUserAndIdIn의 반환 값을 설정
        when(cartRepository.findByUserAndIdIn(user, cartIds)).thenReturn(carts);

        // 메서드 실행
        cartService.purchaseFromCart(cartIdListAndAddress, user);

        // 검증
        verify(cartRepository, times(1)).findByUserAndIdIn(user, cartIds);
        verify(productRepository, times(1)).updatePurchaseCountInBatch(anySet(), anyInt()); // productRepository의 updatePurchaseCountInBatch 호출 검증
        verify(userRepository, times(1)).save(user);
        verify(orderRepository, times(1)).saveAll(anyList());
        verify(cartRepository, times(1)).deleteAll(carts);
    }
}



