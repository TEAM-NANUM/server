package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.*;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    public void createReview(AddReviewDTO dto){ //리뷰 작성
        Order order = orderRepository.findById(dto.orderId()) //리뷰 작성을 위한 주문 찾기
                .orElseThrow(()-> new NotFoundException("존재하지 않는 주문입니다."));
        Review review = dto.toEntity(order);
        reviewRepository.save(review);
        order.setReview(review); //리뷰는 제품 정보를 가지고 있지  않기 때문에 별점 계산을 위해 주문에 리뷰 정보를 넣음
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(order.getProduct()); //제품이 가지고 있는 모든 주문 가져오기
        Float ratingAll = (float) 0;
        int reviewedCount = 0;
        for(Order orderData: orderList){ //별점 총합 구하기 TODO: 리팩토링 방법 있으면 사용
            if(orderData.getReview()!=null){
                ratingAll+= orderData.getReview().getRating();
                reviewedCount++;
            }
        }
        order.getProduct().setRatingAvg(ratingAll/(reviewedCount)); //평균 별점 변경

    }
    public MyUnReviewOrdersDTO GetUnReviewOrder(User user){ //리뷰 안달린 주문 모두 구하기
        List<Order> orderList = orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString()); //리뷰가 null이고 배달이 완료된 주문 찾기
        return MyUnReviewOrdersDTO.toEntity(orderList);
    }
    public MyReviewOrdersDTO GetReviewedOrder(User user){ //리뷰 달린 주문 모두 구하기
        List<Order> orderList = orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString()); //리뷰가 작성됐고 배달이 완료된 주문 찾기
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
