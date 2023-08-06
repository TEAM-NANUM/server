package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.response.MyCompleteOrdersDTO;
import server.nanum.dto.response.MyProgressOrdersDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.service.OrderService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<Void> createOrder(@RequestBody AddOrderDTO dto, @CurrentUser User user){
        orderService.createOrder(dto,user);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user")
    public ResponseEntity<OrderUserInfoDTO> getUserInfo(@CurrentUser User user){
        OrderUserInfoDTO dto = orderService.getUserDefaultInfo(user);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/in-progress")
    public ResponseEntity<MyProgressOrdersDTO> getProgressOrders(@CurrentUser User user){
        MyProgressOrdersDTO dto = orderService.getInProgressOrder(user);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/complete")
    public ResponseEntity<MyCompleteOrdersDTO> getCompleteOrders(@CurrentUser User user){
        MyCompleteOrdersDTO dto = orderService.getCompleteOrder(user);
        return ResponseEntity.ok().body(dto);
    }
}
