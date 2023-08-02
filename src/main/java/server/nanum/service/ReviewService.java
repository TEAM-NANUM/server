package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.*;
import server.nanum.dto.request.AddReviewDTO;
import server.nanum.dto.response.MyUnReviewOrdersDTO;
import server.nanum.dto.response.MyReviewOrdersDTO;
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
    public void createReview(AddReviewDTO dto){
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(()-> new RuntimeException());
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
}
