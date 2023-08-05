package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.*;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.ProductDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.ReviewRepository;
import server.nanum.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    public void createReview(AddReviewDTO dto){
        ///TODO: 404 에러 처리
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(()-> new RuntimeException("404"));
        Review review = dto.toEntity(order);
        reviewRepository.save(review);
        order.setReview(review);
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(order.getProduct());
        Float ratingAll = (float) 0;
        for(Order orderData: orderList){
            ratingAll+= orderData.getReview().getRating();
        }
        order.getProduct().setRatingAvg(ratingAll/(orderList.size()));

    }
    public MyUnReviewOrdersDTO GetUnReviewOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyUnReviewOrdersDTO.toEntity(orderList);
    }
    public MyReviewOrdersDTO GetReviewedOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyReviewOrdersDTO.toEntity(orderList);
    }

    public ProductReviewDTO.ReviewList getProductReviews(Long productId) {
        productRepository.findById(productId).orElseThrow(()->new NotFoundException("존재하지 않는 상품입니다."));

        List<Review> result = reviewRepository.findAllByOrderProductId(productId);

        List<ProductReviewDTO.ReviewListItem> reviewItems = result.stream()
                .map(review -> ProductReviewDTO.ReviewListItem.builder()
                        .id(review.getId())
                        .username(review.getOrder().getUser().getName())
                        .rating(review.getRating())
                        .comment(review.getComment()).build())
                .toList();

        return ProductReviewDTO.ReviewList.builder()
                .reviews(reviewItems)
                .build();
    }
}
