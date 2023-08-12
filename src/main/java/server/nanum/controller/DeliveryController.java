package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.delivery.DeliveryListResponse;
import server.nanum.dto.delivery.DeliveryRequestDTO;
import server.nanum.service.DeliveryService;

/**
 * 배송지 관리 컨트롤러
 * 사용자의 배송지 조회, 추가, 기본 배송지 변경, 수정, 삭제 API를 제공합니다.
 *@author hyunjin
 * @version 1.0.0
 * @since 2023-08-05
 */
@Tag(name = "배송지 관리 API", description = "배송지 관련 API입니다. 배송지 조회, 추가, 기본 배송지 변경 기능을 제공합니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-address")
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 사용자의 배송지 목록 조회 API
     *
     * @param user 현재 사용자 정보
     * @return DeliveryListResponse 사용자의 배송지 목록
     */
    @Operation(summary = "배송지 목록 조회 API", description = "사용자의 모든 배송지를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(schema = @Schema(implementation = DeliveryListResponse.class))),
            @ApiResponse(responseCode = "500", description= "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_HOST')")
    public DeliveryListResponse getDeliveryList(@CurrentUser User user) {
        return deliveryService.getDeliveryList(user);
    }

    /**
     * 새로운 배송지 정보 저장 API
     *
     * @param request 배송지 정보 요청 DTO
     * @param user 현재 사용자 정보
     * @return ResponseEntity<Void> 배송지 저장 결과 응답
     */
    @Operation(summary = "배송지 정보 저장 API", description = "새로운 배송지 정보를 저장하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 성공!",  content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<Void> saveDelivery(@RequestBody DeliveryRequestDTO request, @CurrentUser User user) {
        deliveryService.saveDelivery(request, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 기본 배송지 변경 API
     *
     * @param id 변경하려는 배송지 ID
     * @param user 현재 사용자 정보
     * @return ResponseEntity<Void> 기본 배송지 변경 결과 응답
     */
    @Operation(summary = "기본 배송지 변경 API", description = "선택된 배송지를 기본 배송지로 설정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description= "ID를 통해 접근한 배송지가 존재하지 않을 떄", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description= "사욪자가 등록한 배송지 중에 is_default가 true, 즉 기본 배송지가 존재할 때", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/default")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<Void> udpateDefault(@RequestParam Long id, @CurrentUser User user) {
        deliveryService.toggleDefault(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 배송지 정보 수정 API
     *
     * @param id 수정하려는 배송지 ID
     * @param request 배송지 정보 요청 DTO
     * @param user 현재 사용자 정보
     * @return ResponseEntity<Void> 배송지 수정 결과 응답
     */
    @Operation(summary = "배송지 정보 수정 API", description = "특정 배송지 정보를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID를 통해 접근한 배송지가 존재하지 않을 때", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<Void> updateDelivery(@PathVariable Long id, @RequestBody DeliveryRequestDTO request, @CurrentUser User user) {
        deliveryService.updateDelivery(id, request, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 배송지 정보 삭제 API
     *
     * @param id 삭제하려는 배송지 ID
     * @param user 현재 사용자 정보
     * @return ResponseEntity<Void> 배송지 삭제 결과 응답
     */
    @Operation(summary = "배송지 정보 삭제 API", description = "특정 배송지 정보를 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "응답 성공!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID를 통해 접근한 배송지가 존재하지 않을 때", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id, @CurrentUser User user) {
        deliveryService.deleteDelivery(id, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

