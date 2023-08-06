package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Cart;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.CartRepository;
import server.nanum.repository.ProductRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    public CartResponseDTO.CartList getCartItems(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);

        List<CartResponseDTO.CartListItem> cartListItems = cartItems.stream()
                .map(CartResponseDTO.CartListItem::toDTO)
                .toList();

        return CartResponseDTO.CartList.toDTO(cartListItems);
    }

    public void addToCart(CartRequestDTO.CartItem cartItem, User user) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        // 장바구니 Item이 존재하지 않는다면 생성
        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> Cart.createEmptyCartItem(user, product));

        // 장바구니 상품 수량 증가
        cart.increaseProductCount(cartItem.getQuantity());

        cartRepository.save(cart);
    }

    public CartResponseDTO.CartListItem updateCartItemQuantity(CartRequestDTO.CartItemQuantity cartItemQuantity, User user) {
        Cart cart = cartRepository.findByIdAndUser(cartItemQuantity.getId(), user)
                .orElseThrow(() -> new NotFoundException("해당하는 장바구니 항목을 찾을 수 없습니다."));

        cart.setProductCount(cartItemQuantity.getQuantity());
        cartRepository.save(cart);

        return CartResponseDTO.CartListItem.toDTO(cart);
    }

    public void removeFromCart(CartRequestDTO.CartIdList cartIdList, User user) {
        List<Long> cartIds = cartIdList.getItemIds();
        List<Cart> cartItems = cartRepository.findByUserAndIdIn(user, cartIds);
        cartRepository.deleteAllInBatch(cartItems);
    }
}
