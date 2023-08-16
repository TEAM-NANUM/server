package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Cart;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.exception.BadRequestException;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.CartRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


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

    public CartResponseDTO.CartList updateCartItemQuantity(CartRequestDTO.CartItemQuantity cartItemQuantity, User user) {
        Cart cart = cartRepository.findByIdAndUser(cartItemQuantity.getId(), user)
                .orElseThrow(() -> new NotFoundException("해당하는 장바구니 항목을 찾을 수 없습니다."));

        cart.setProductCount(cartItemQuantity.getQuantity());
        cartRepository.save(cart);

        return getCartItems(user);
    }

    public CartResponseDTO.CartList removeFromCart(CartRequestDTO.CartIdList cartIdList, User user) {
        List<Long> cartIds = cartIdList.getItemIds();
        List<Cart> cartItems = cartRepository.findByUserAndIdIn(user, cartIds);
        cartRepository.deleteAllInBatch(cartItems);

        return getCartItems(user);
    }

    public void purchaseFromCart(CartRequestDTO.CartIdListAndAddress cartIdListAndAddress, User user) {
        List<Long> cartIds = cartIdListAndAddress.getItemIds();
        List<Cart> carts = cartRepository.findByUserAndIdIn(user, cartIds);

        List<Long> foundCartIds = carts
                .stream()
                .map(Cart::getId)
                .toList();

        for (Long cartId : cartIds) {
            if (!foundCartIds.contains(cartId)) {
                throw new NotFoundException("존재하지 않는 장바구니 항목입니다: " + cartId);
            }
        }

        int totalAmount = carts.stream()
                .mapToInt(cart -> cart.getProductCount() * cart.getProduct().getPrice())
                .sum();

        int userPoint = user.getUserGroupPoint();
        if (userPoint < totalAmount) {
            throw new BadRequestException("보유 포인트가 모자릅니다!");
        }

        List<Order> orders = new ArrayList<>();
        Map<Product, Integer> productPurchaseCountMap = new HashMap<>();
        for (Cart cart : carts) {
            Product product = cart.getProduct();
            int productCount = cart.getProductCount();
            int totalPriceForProduct = productCount * product.getPrice();

            productPurchaseCountMap.merge(product, productCount, Integer::sum);

            orders.add(Order.builder()
                    .product(product)
                    .productCount(productCount)
                    .totalAmount(totalPriceForProduct)
                    .user(user)
                    .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETE)
                    .deliveryAddress(cartIdListAndAddress.getAddress().toString())
                    .build());
        }
        
        for (Map.Entry<Product, Integer> entry : productPurchaseCountMap.entrySet()) {
            productRepository.updatePurchaseCountInBatch(Collections.singleton(entry.getKey()), entry.getValue());
        }

        user.getUserGroup().updatePoint(userPoint - totalAmount);
        userRepository.save(user);
        orderRepository.saveAll(orders);
        cartRepository.deleteAll(carts);
    }
}
