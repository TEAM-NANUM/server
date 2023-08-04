package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.request.AddReviewDto;
import server.nanum.dto.response.MyUnReviewOrdersDto;
import server.nanum.dto.response.MyReviewOrdersDto;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.service.ReviewService;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/reviews/delivered")
    public ResponseEntity<MyUnReviewOrdersDto> getDeliveredOrder(){
        Long userId=1L; // 가상으로 지정
        MyUnReviewOrdersDto dto = reviewService.GetUnReviewOrder(userId);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/reviews/my")
    public ResponseEntity<MyReviewOrdersDto> getMyOrder(){
        Long userId=1L; // 가상으로 지정
        MyReviewOrdersDto dto = reviewService.GetReviewedOrder(userId);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping("/reviews")
    public ResponseEntity<Void> addReview(@RequestBody AddReviewDto dto){
        reviewService.createReview(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{product_id}/reviews")
    public ResponseEntity<ProductReviewDTO.ReviewList> getProductReviews(@PathVariable("product_id") Long productId) {
        ProductReviewDTO.ReviewList productReviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(productReviews);
    }
}
