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
    public void createOrder(AddOrderDTO dto, User user){
        // TODO: 404 오류 처리
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(()-> new RuntimeException("404"));
        //ToDO: 402 오류 처리 (포인트 부족)
        if(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice()<0){
            throw new RuntimeException("402");
        }
        user.getUserGroup().setPoint(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice());
        Order order = dto.toEntity(product,user);
        orderRepository.save(order);

    }
    public OrderUserInfoDTO getUserDefaultInfo(User user){
        // TODO: 404 오류 처리
        Delivery delivery = deliveryRepository.findByUserAndIsDefaultTrue(user)
                        .orElseThrow(()-> new RuntimeException("404"));
        return OrderUserInfoDTO.toEntity(delivery);
    }

    public MyProgressOrdersDTO getInProgressOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.IN_PROGRESS.toString());
        return MyProgressOrdersDTO.toEntity(orderList);
    }
    public MyCompleteOrdersDTO getCompleteOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyCompleteOrdersDTO.toEntity(orderList);
    }

}
