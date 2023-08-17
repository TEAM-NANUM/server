package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.request.CartRequestDTO;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.service.CartService;

/**
 * 장바구니 관련 컨트롤러
 * 장바구니 관련 API를 제공합니다.
 *@author Jinyenong Seol
 * @version 1.0.0
 * @since 2023-08-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "장바구니 관련 API", description = "장바구니 아이템을 관리하는 API를 제공합니다.")
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    /**
     * 현재 사용자의 장바구니 아이템을 가져오는 API
     *
     * @param user 현재 사용자 정보
     * @return 장바구니 아이템 목록
     */
    @Operation(summary = "현재 사용자의 장바구니 아이템을 가져오는 API", description = "현재 사용자의 장바구니 목록을 불러오는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = CartResponseDTO.CartList.class))),
            @ApiResponse(responseCode = "403", description= "장바구니 목록 요청 권한이 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping
    public ResponseEntity<CartResponseDTO.CartList> getCartItems(@CurrentUser User user) {
        CartResponseDTO.CartList cartItems = cartService.getCartItems(user);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * 장바구니에 아이템을 추가하는 API
     *
     * @param user      현재 사용자 정보
     * @param cartItem  장바구니에 추가할 아이템 정보
     * @return 처리 결과
     */
    @Operation(summary = "장바구니에 아이템을 추가하는 API", description = "현재 사용자의 장바구니에 아이템을 추가하는 API 입니다. 처음 추가되는 경우 아이템이 생성되고," +
            " 기존에 장바구니에 존재하던" + "아이템에 대해서는 수량을 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 추가 성공!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description= "장바구니 추가를 요청한 상품이 존재하지 않음.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping
    public ResponseEntity<Void> addToCart(@CurrentUser User user, @Valid @RequestBody CartRequestDTO.CartItem cartItem) {
        cartService.addToCart(cartItem, user);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 아이템의 수량을 업데이트하는 API
     *
     * @param user              현재 사용자 정보
     * @param cartItemQuantity  업데이트할 아이템 수량 정보
     * @return 업데이트된 아이템 정보
     */
    @Operation(summary = "장바구니 아이템의 수량을 업데이트하는 API", description = "현재 사용자의 장바구니 아이템의 수량을 업데이트 하는 API 입니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description= "변경 할 장바구니 아이템이 존재하지 않음.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PatchMapping
    public ResponseEntity<CartResponseDTO.CartList> updateCartItemQuantity(@CurrentUser User user, @Valid @RequestBody CartRequestDTO.CartItemQuantity cartItemQuantity) {
        CartResponseDTO.CartList cartList = cartService.updateCartItemQuantity(cartItemQuantity, user);
        return ResponseEntity.ok(cartList);
    }

    /**
     * 장바구니에서 아이템을 제거하는 API
     *
     * @param user       현재 사용자 정보
     * @param cartIdList 제거할 아이템 ID 목록
     * @return 처리 결과
     */
    @Operation(summary = "장바구니에서 아이템을 제거하는 API", description = "현재 사용자의 장바구니에 대해, 장바구니 아이템을 다중 선택하여 제거할 수 있도록 하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description= "삭제 할 장바구니 아이템이 존재하지 않음.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping("/delete")
    public ResponseEntity<CartResponseDTO.CartList> removeFromCart(@CurrentUser User user, @Valid @RequestBody CartRequestDTO.CartIdList cartIdList) {
        CartResponseDTO.CartList cartList = cartService.removeFromCart(cartIdList, user);
        return ResponseEntity.ok(cartList);
    }


    @Operation(summary = "장바구니에서 아이템을 일괄 구매하는 API", description = "장바구니 항목의 Id를 받아서 일괄 구매를 진행하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 성공!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description= "삭제 할 장바구니 아이템이 존재하지 않음.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseFromCart(@CurrentUser User user, @Valid @RequestBody CartRequestDTO.CartIdListAndAddress cartIdListAndAddress) {
        cartService.purchaseFromCart(cartIdListAndAddress, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
