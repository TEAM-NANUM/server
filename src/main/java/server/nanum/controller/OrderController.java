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
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.User;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.response.MyOrderListDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.service.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * 주문 관련 컨트롤러
 * 주문과 관련된 API를 제공합니다.
 *
 * @author 김민규
 * @version 1.0.0
 * @since 2023-08-10
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Tag(name="주문 API",description = "주문 관련 API입니다. 진행중인 주문 내역 조회, 완료된 주문 내역 조회, 상품 구매용 유저 정보 조회, 주문 추가(상품구매)를 수행합니다.")
@PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 생성 API
     *
     * @param dto 주문에 필요한 정보
     * @param user 현재 사용자 정보를 가져옴
     * @return ResponseEntity<Void> 주문 생성 결과 응답
     *
     */

    @Operation(summary = "주문 추가 API", description = "사용자가 상품 ID와 개수, 주소를 입력해 주문을 추가하는 API입니다. (API명세서 26번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 추가 성공", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "요청에 누락이 있는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "402", description = "포인트가 부족한 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "상품 ID로 상품을 찾을 수 없는 경우 ", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping()
    public ResponseEntity<Void> createOrder(@Valid @RequestBody AddOrderDTO dto, @CurrentUser User user){
        orderService.createOrder(dto,user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품 구매시 유저 정보 조회 API
     *
     * @param user 현재 사용자 정보를 가져옴
     * @return ResponseEntity<OrderUserInfoDTO> 사용자 정보 응답
     *
     */

    @Operation(summary = "상품 구매용 유저 정보 조회 API", description = "상품 구매 페이지에서 사용자의 기본 정보를 가져오기 위한 API입니다. (API명세서 25번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation =OrderUserInfoDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/user")
    public ResponseEntity<OrderUserInfoDTO> getUserInfo(@CurrentUser User user){
        OrderUserInfoDTO dto = orderService.getUserDefaultInfo(user);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 현재 배송 진행중인 주문 조회 API
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return ResponseEntity<MyOrderListDTO> 사용자의 현재 진행 중인 주문 정보와 개수 응답
     */

    @Operation(summary = "진행중인 주문 내역 조회 API", description = "사용자의 주문 중 현재 배송이 진행중인 주문 내역을 조회하는 API입니다. (API명세서 23번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation =MyOrderListDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/in-progress")
    public ResponseEntity<MyOrderListDTO> getProgressOrders(@CurrentUser User user){
        List<DeliveryStatus> deliveryStatusList = new ArrayList<>();
        deliveryStatusList.add(DeliveryStatus.PAYMENT_COMPLETE);
        deliveryStatusList.add(DeliveryStatus.IN_PROGRESS);
        MyOrderListDTO dto = orderService.getUserOrder(user, deliveryStatusList);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 배송 종료된 주문 조회 API
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return ResponseEntity<MyOrderListDTO> 사용자의 배송 종료된 주문 정보와 개수 응답
     */

    @Operation(summary = "완료된 주문 내역 조회 API", description = "사용자의 주문 중 배송이 완료된 주문 내역을 조회하는 API입니다. (API명세서 24번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation =MyOrderListDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/complete")
    public ResponseEntity<MyOrderListDTO> getCompleteOrders(@CurrentUser User user){
        MyOrderListDTO dto = orderService.getUserOrder(user, DeliveryStatus.DELIVERED);
        return ResponseEntity.ok().body(dto);
    }
}
