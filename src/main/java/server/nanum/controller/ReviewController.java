package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.service.ReviewService;
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name="리뷰 작성 API",description = "리뷰 작성 관련 API입니다. 리뷰 작성 안 된 주문 조회, 리뷰 작성 된 주문 조회, 리뷰 작성, 상품의 리뷰 목록 조회를 수행합니다.")
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    @Operation(summary = "리뷰 작성 안 된 주문 조회 API", description = "사용자의 주문 중 사용자가 리뷰를 작성하지 않은 주문을 가져오기 위한 API입니다. (API명세서 27번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = MyUnReviewOrdersDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping("/reviews/delivered")
    public ResponseEntity<MyUnReviewOrdersDTO> getDeliveredOrder(@CurrentUser User user){
        MyUnReviewOrdersDTO dto = reviewService.GetUnReviewOrder(user);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "리뷰 작성 된 주문 조회 API", description = "사용자의 주문 중 사용자가 리뷰를 작성한 주문을 가져오기 위한 API입니다. (API명세서 28번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = MyReviewOrdersDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping("/reviews/my")
    public ResponseEntity<MyReviewOrdersDTO> getMyOrder(@CurrentUser User user){
        MyReviewOrdersDTO dto = reviewService.GetReviewedOrder(user);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "리뷰 작성 API", description = "사용자가 주문 ID와 별점, 후기를 입력해 리뷰를 추가하는 API입니다. (API명세서 29번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 추가 성공", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "요청에 누락이 있는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "주문 ID로 주문을 찾을 수 없는 경우 ", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping("/reviews")
    public ResponseEntity<Void> addReview(@Valid @RequestBody AddReviewDTO dto){
        reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품의 리뷰 목록 조회 API", description = "상품 ID로 해당 상품에 작성된 리뷰를 모두 가져오는 API 입니다. (API명세서 11번)")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = ProductReviewDTO.ReviewList.class))),
            @ApiResponse(responseCode = "404", description = "상품 ID로 상품을 찾을 수 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping("/products/{product_id}/reviews")
    public ResponseEntity<ProductReviewDTO.ReviewList> getProductReviews(
            @PathVariable("product_id") Long productId) {
        ProductReviewDTO.ReviewList productReviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(productReviews);
    }
}
