package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.Request.RequestReviewDto;
import server.nanum.dto.Response.ResponseReviewOrderDto;
import server.nanum.dto.Response.ResponseUnReviewOrderDto;
import server.nanum.service.ReviewService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews/")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/delivered")
    public ResponseEntity<ResponseUnReviewOrderDto> getDeliveredOrder(){
        Long userId=1L; // 가상으로 지정
        ResponseUnReviewOrderDto dto = reviewService.GetUnReviewOrder(userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/my")
    public ResponseEntity<ResponseReviewOrderDto> getMyOrder(){
        Long userId=1L; // 가상으로 지정
        ResponseReviewOrderDto dto = reviewService.GetReviewedOrder(userId);
        return ResponseEntity.ok().build();
    }
    @PostMapping()
    public ResponseEntity<Void> addReview(@RequestBody RequestReviewDto dto){
        reviewService.createReview(dto);
        return ResponseEntity.ok().build();
    }
}
