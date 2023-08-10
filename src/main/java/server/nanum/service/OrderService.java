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
import server.nanum.exception.NotFoundException;
import server.nanum.exception.PaymentRequiredException;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;

import java.util.List;

/**
 * 주문 관리 서비스 클래스
 * 주문의 추가, 조회, 주문에 필요한 유저 정보 조회를 처리합니다
 *@author 김민규
 * @version 1.0.0
 * @since 2023-08-10
 */

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 새로운 주문 생성을 수행합니다
     *
     * @param dto 주문에 필요한 정보
     * @param user 현재 사용자의 정보를 가져옴
     * @return 주문 생성 완료 응답
     * @throws NotFoundException 상품Id로 찾은 상품이 존재하지 않을 경우 예외를 던집니다.
     * @throws PaymentRequiredException 주문을 위한 포인트가 부족할 경우 예외를 던집니다
     */
    public void createOrder(AddOrderDTO dto, User user){
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(()-> new NotFoundException("존재하지 않는 제품입니다"));
        if(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice()<0){
            throw new PaymentRequiredException("포인트가 부족합니다");
        }
        user.getUserGroup().updatePoint(user.getUserGroup().getPoint()-dto.quantity()*product.getPrice());
        Order order = dto.toEntity(product,user);
        orderRepository.save(order);
    }

    /**
     * 주문 생성에 필요한 사용자 기본정보 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return OrderUserInfoDTO 사용자의 정보 응답
     * @throws NotFoundException 사용자의 기본 배송지가 존재하지 않는 경우
     */
    public OrderUserInfoDTO getUserDefaultInfo(User user){
        Delivery delivery = deliveryRepository.findByUserAndIsDefaultTrue(user) //만약 없으면 배송지 아무거나 찾고 아예 아무것도 없으면 오류 처리로 바꿀까?
                .orElseThrow(()-> new NotFoundException("기본 배송지가 존재하지 않습니다."));
        return OrderUserInfoDTO.toEntity(delivery);
    }

    /**
     * 현재 배송 진행중인 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return MyPrgressOrdersDTO 사용자의 현재 진행 중인 주문 정보와 개수
     */
    public MyProgressOrdersDTO getInProgressOrder(User user){ //진행중인 주문 정보
        List<Order> orderList1 = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.IN_PROGRESS.toString());
        /* 만약 결제 완료된 주문도 포함시킬 경우
        List<Order> orderList2 = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.PAYMENT_COMPLETE.toString());
        orderList1.addAll(orderList2);*/
        return MyProgressOrdersDTO.toEntity(orderList1);
    }

    /**
     * 배송 종료된 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @return MyCompleteOrdersDTO 사용자의 배송 종료된 주문 정보와 개수
     */
    public MyCompleteOrdersDTO getCompleteOrder(User user){
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, DeliveryStatus.DELIVERED.toString());
        return MyCompleteOrdersDTO.toEntity(orderList);
    }

}
