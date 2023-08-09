package server.nanum.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.service.CartService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping
    public ResponseEntity<CartResponseDTO.CartList> getCartItems(@CurrentUser User user) {
        CartResponseDTO.CartList cartItems = cartService.getCartItems(user);
        return ResponseEntity.ok(cartItems);
    }

    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping
    public ResponseEntity<Void> addToCart(@CurrentUser User user,@Valid @RequestBody CartRequestDTO.CartItem cartItem) {
        cartService.addToCart(cartItem, user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PatchMapping
    public ResponseEntity<CartResponseDTO.CartListItem> updateCartItemQuantity(@CurrentUser User user, @Valid @RequestBody CartRequestDTO.CartItemQuantity cartItemQuantity) {
        CartResponseDTO.CartListItem cartListItem = cartService.updateCartItemQuantity(cartItemQuantity, user);
        return ResponseEntity.ok(cartListItem);
    }

    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping("/delete")
    public ResponseEntity<Void> removeFromCart(@CurrentUser User user, @RequestBody CartRequestDTO.CartIdList cartIdList) {
        cartService.removeFromCart(cartIdList, user);
        return ResponseEntity.ok().build();
    }
}
