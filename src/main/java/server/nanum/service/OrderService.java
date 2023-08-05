package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Delivery;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.response.MyCompleteOrdersDTO;
import server.nanum.dto.response.MyProgressOrdersDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    public void createOrder(AddOrderDTO dto, User user){ //주문 생성
        // TODO: 404 오류 처리
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(()-> new RuntimeException("404"));
        //ToDO: 402 오류 처리 (포인트 부족)
        if(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice()<0){ //포인트 부족 시 402 오류
            throw new RuntimeException("402");
        }
        user.getUserGroup().setPoint(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice()); //주문 시 포인트 계산
        Order order = dto.toEntity(product,user);
        orderRepository.save(order);
    }
    public OrderUserInfoDTO getUserDefaultInfo(User user){ //주문 시 사용하는 유저 정보
        // TODO: 404 오류 처리
        Delivery delivery = deliveryRepository.findByUserAndIsDefaultTrue(user) //기본 배송지 찾기 만약 없으면 404 -> 만약 없으면 배송지 아무거나 찾아오는 것으로 바꿀까?
                        .orElseThrow(()-> new RuntimeException("404"));
        return OrderUserInfoDTO.toEntity(delivery);
    }

    public MyProgressOrdersDTO getInProgressOrder(User user){ //진행중인 주문 정보
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.IN_PROGRESS.toString()); //진행중인 주문 최신순으로 찾기
        return MyProgressOrdersDTO.toEntity(orderList);
    }
    public MyCompleteOrdersDTO getCompleteOrder(User user){ // 배송 완료된 주문 정보
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString()); //완료된 주문 최신순으로 찾기
        return MyCompleteOrdersDTO.toEntity(orderList);
    }

}
