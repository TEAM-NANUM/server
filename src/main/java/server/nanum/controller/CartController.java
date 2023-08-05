package server.nanum.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<CartResponseDTO.CartList> getCartItems(@CurrentUser User user) {
        CartResponseDTO.CartList cartItems = cartService.getCartItems(user);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping()
    public ResponseEntity<Void> addToCart(@CurrentUser User user, @RequestBody CartRequestDTO.CartItem cartItem) {
        cartService.addToCart(cartItem, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> removeFromCart(@CurrentUser User user, @RequestBody CartRequestDTO.CartIdList cartIdList) {
        cartService.removeFromCart(cartIdList, user);
        return ResponseEntity.ok().build();
    }
}
