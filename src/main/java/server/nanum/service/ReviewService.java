package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
import server.nanum.domain.User;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.ProductReviewDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.ReviewRepository;

import java.util.List;

/**
 * 리뷰 관리 서비스 클래스
 * 리뷰의 추가, 조회, 주문의 조회를 처리합니다
 *@author 김민규
 * @version 1.0.0
 * @since 2023-08-10
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 새로운 리뷰 생성을 수행합니다
     *
     * @param dto 리뷰에 필요한 정보
     * @return 리뷰 생성 완료 응답
     * @throws NotFoundException 주문Id로 찾은 주문이 존재하지 않을 경우 예외를 던집니다.
     */
    @Transactional
    public void createReview(AddReviewDTO dto){
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(()-> new NotFoundException("존재하지 않는 주문입니다."));
        Review review = dto.toEntity(order);
        reviewRepository.save(review);
        order.setReview(review);
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(order.getProduct());
        Float ratingAll = (float) 0;
        int reviewedCount = 0;
        for(Order orderData: orderList){ //TODO: 리팩토링 방법 있으면 사용
            if(orderData.getReview()!=null){
                ratingAll+= orderData.getReview().getRating();
                reviewedCount++;
            }
        }
        order.getProduct().setRatingAvg(ratingAll/(reviewedCount));

    }

    /**
     * 리뷰가 없는 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return MyUnReviewOrdersDTO 사용자의 리뷰가 없는 주문 정보와 그 개수
     */
    public MyUnReviewOrdersDTO getUnReviewOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyUnReviewOrdersDTO.toEntity(orderList);
    }

    /**
     * 리뷰가 있는 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return MyReviewOrdersDTO 사용자의 리뷰가 있는(사용자가 리뷰를 작성 한) 주문 정보와 그 개수
     */
    public MyReviewOrdersDTO getReviewedOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyReviewOrdersDTO.toEntity(orderList);
    }

    /**
     * 상품의 리뷰 전체 조회를 수행합니다
     *
     * @param productId 상품의 Id
     * @return ProductReviewDTO.ReviewList 상품의 리뷰 정보
     * @throws NotFoundException 상품Id로 찾은 상품이 존재하지 않을 경우 예외를 던집니다.
     */
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
