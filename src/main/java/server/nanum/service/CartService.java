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
import java.util.Optional;

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
                .map(cart -> CartResponseDTO.CartListItem.builder()
                        .id(cart.getId())
                        .imgUrl(cart.getProduct().getImgUrl())
                        .totalPrice((cart.getProduct().getPrice() * cart.getProductCount()))
                        .quantity(cart.getProductCount())
                        .build())
                .toList();

        return CartResponseDTO.CartList.builder().items(cartListItems).build();
    }

    public void addToCart(CartRequestDTO.CartItem cartItem, User user) {
        Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(()-> new NotFoundException("존재하지 않는 상품입니다."));

        Cart cart = cartRepository.findByUserAndProduct(user, product);

        if (cart != null) {
            // 이미 장바구니에 해당 상품이 존재할 경우, 수량만 업데이트
            cart.setProductCount(cart.getProductCount() + cartItem.getQuantity());
        } else {
            // 장바구니에 해당 상품이 없을 경우, 신규로 추가
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setProductCount(cartItem.getQuantity());
        }
        cartRepository.save(cart);
    }

    public void removeFromCart(CartRequestDTO.CartIdList cartIdList, User user) {
        List<Long> cartIds = cartIdList.getItemIds();
        List<Cart> cartItems = cartRepository.findByUserAndIdIn(user, cartIds);
        cartRepository.deleteAllInBatch(cartItems);
    }
}
