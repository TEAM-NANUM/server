package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.*;
import server.nanum.domain.product.Product;
import server.nanum.dto.request.AddOrderDTO;
import server.nanum.dto.response.MyOrderListDTO;
import server.nanum.dto.response.OrderUserInfoDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.exception.PaymentRequiredException;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
     * 한개의 배송 상태로 사용자의 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @param deliveryStatus 배송 상태 enum을 가져옴
     * @return MyOrderListDTO 사용자의 현재 진행 중인 주문 정보와 개수
     */
    public MyOrderListDTO getUserOrder(User user,DeliveryStatus deliveryStatus){ //진행중인 주문 정보
        /* host가 조회 시 그룹원들 주문도 조회
        List<Order> orderList;
        if(user.getUserRole() == UserRole.HOST){
            orderList = orderRepository.findByUserUserGroupAndDeliveryStatusOrderByCreateAtDesc(user.getUserGroup(),deliveryStatus.toString());
        }else{
            orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, deliveryStatus.toString());
        }*/

        //위에 주석 사용 시 이 코드 주석처리 해야함
        List<Order> orderList = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, deliveryStatus.toString());

        return MyOrderListDTO.toEntity(orderList);
    }

    /**
     * 두개 이상의 배송 상태로 사용자의 주문 조회를 수행합니다
     *
     * @param user 현재 사용자의 정보를 가져옴
     * @param deliveryStatusList 배송 상태 enum의 List를 가져옴
     * @return MyOrderListDTO 사용자의 현재 진행 중인 주문 정보와 개수
     */
    public MyOrderListDTO getUserOrder(User user,List<DeliveryStatus> deliveryStatusList){
        Set<Order> set = new LinkedHashSet<>();
        for(DeliveryStatus deliveryStatus:deliveryStatusList){
            /* host가 조회 시 그룹원들 주문도 조회
            List<Order> orderListSub;
            if(user.getUserRole() == UserRole.HOST){
                orderListSub = orderRepository.findByUserUserGroupAndDeliveryStatusOrderByCreateAtDesc(user.getUserGroup(),deliveryStatus.toString());
            }else{
                orderListSub = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, deliveryStatus.toString());
            }*/

            //위에 주석 사용 시 이 코드 주석처리 해야함
            List<Order> orderListSub = orderRepository.findByUserAndDeliveryStatusOrderByCreateAtDesc(user, deliveryStatus.toString());

            set.addAll(orderListSub);
        }
        List<Order> orderList= new ArrayList<>(set);
        return MyOrderListDTO.toEntity(orderList);
    }



}
