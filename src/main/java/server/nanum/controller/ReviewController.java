package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.service.ReviewService;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/delivered")
    public ResponseEntity<MyUnReviewOrdersDTO> getDeliveredOrder(@CurrentUser User user){
        MyUnReviewOrdersDTO dto = reviewService.GetUnReviewOrder(user);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/my")
    public ResponseEntity<MyReviewOrdersDTO> getMyOrder(@CurrentUser User user){
        MyReviewOrdersDTO dto = reviewService.GetReviewedOrder(user);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping()
    public ResponseEntity<Void> addReview(@RequestBody AddReviewDTO dto){
        reviewService.createReview(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{product_id}/reviews")
    public ResponseEntity<ProductReviewDTO.ReviewList> getProductReviews(@PathVariable("product_id") Long productId) {
        ProductReviewDTO.ReviewList productReviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(productReviews);
    }
}
