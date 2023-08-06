package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.service.ReviewService;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping("/reviews/delivered")
    public ResponseEntity<MyUnReviewOrdersDTO> getDeliveredOrder(@CurrentUser User user){
        MyUnReviewOrdersDTO dto = reviewService.GetUnReviewOrder(user);
        return ResponseEntity.ok().body(dto);
    }
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping("/reviews/my")
    public ResponseEntity<MyReviewOrdersDTO> getMyOrder(@CurrentUser User user){
        MyReviewOrdersDTO dto = reviewService.GetReviewedOrder(user);
        return ResponseEntity.ok().body(dto);
    }
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @PostMapping("/reviews")
    public ResponseEntity<Void> addReview(@RequestBody AddReviewDTO dto){
        reviewService.createReview(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{product_id}/reviews")
    public ResponseEntity<ProductReviewDTO.ReviewList> getProductReviews(@PathVariable("product_id") Long productId) {
        ProductReviewDTO.ReviewList productReviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(productReviews);
    }
}
