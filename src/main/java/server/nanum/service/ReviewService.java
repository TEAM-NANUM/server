package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
import server.nanum.domain.User;
import server.nanum.dto.request.AddReviewDto;
import server.nanum.dto.response.MyUnReviewOrdersDto;
import server.nanum.dto.response.MyReviewOrdersDto;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ReviewRepository;
import server.nanum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    public void createReview(AddReviewDto dto){
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(()-> new RuntimeException());
        Review review = dto.toEntity(order);
        reviewRepository.save(review);
        order.setReview(review);
    }

    public MyUnReviewOrdersDto GetUnReviewOrder(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException());
        List<Order> orderList = orderRepository.findByUserAndReviewIsNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyUnReviewOrdersDto.toEntity(orderList);
    }
    public MyReviewOrdersDto GetReviewedOrder(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException());
        List<Order> orderList = orderRepository.findByUserAndReviewIsNotNullAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyReviewOrdersDto.toEntity(orderList);
    }
}
