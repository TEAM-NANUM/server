package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.Seller;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddProductDTO;
import server.nanum.dto.response.SellerInfoDTO;
import server.nanum.dto.response.SellerOrdersDTO;
import server.nanum.dto.response.SellerProductsDTO;
import server.nanum.exception.BadRequestException;
import server.nanum.exception.ConflictException;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.SubCategoryRepository;
import server.nanum.security.custom.CustomAccessDeniedHandler;

import java.util.List;

/**
 * 판매자 관리 서비스 클래스
 * 판매자의 상품 추가, 조회, 상품의 주문 조회, 판매자의 정보 조회를 처리합니다
 *@author 김민규
 * @version 1.0.0
 * @since 2023-08-10
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SellerService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;

    /**
     * 새로운 상품 생성을 수행합니다
     *
     * @param dto 상품에 필요한 정보
     * @param seller 현재 판매자(사용자)의 정보를 가져옴
     * @return 상품 생성 완료 응답
     * @throws NotFoundException 서브카테고리Id로 찾은 서브카테고리가 존재하지 않을 경우 예외를 던집니다.
     * @throws ConflictException 상품이 이미 존재하는 상품일 경우 예외를 던집니다 (상품 이름으로 확인)
     */
    @Transactional
    public void createProduct(Seller seller, AddProductDTO dto){ //제품 등록
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())
                .orElseThrow(()-> new NotFoundException("존재하지 않는 카테고리입니다."));
        Product product = dto.toEntity(seller,subCategory);
        if(productRepository.existsByName(product.getName())){
            throw new ConflictException("이미 존재하는 제품입니다");
        }
        productRepository.save(product);
    }

    /**
     * 판매자 기본정보 조회를 수행합니다
     *
     * @param seller 현재 판매자(사용자)의 정보를 가져옴
     * @return SellerInfoDTO 판매자의 정보 응답
     */

    public SellerInfoDTO getSellerInfo(Seller seller){ //판매자 정보 가져오기
        return new SellerInfoDTO(seller.getName(),seller.getPoint());
    }

    /**
     * 판매자 등록상품 조회를 수행합니다
     *
     * @param seller 현재 판매자(사용자)의 정보를 가져옴
     * @return SellerProductsDTO 판매자가 등록한 상품 정보와 그 개수 응답
     */

    public SellerProductsDTO getSellerProducts(Seller seller){
        List<Product> productList = productRepository.findAllBySellerOrderByCreateAt(seller);
        return SellerProductsDTO.toEntity(productList);
    }

    /**
     * 상품에 등록된 주문 조회를 수행합니다
     *
     * @param productId 상품의 Id
     * @return SellerOrdersDTO 상품의 주문 정보와 그 개수 응답
     * @throws NotFoundException 상품Id로 찾은 상품이 존재하지 않을 경우 예외를 던집니다.
     * @throws BadRequestException 자신이 등록한 것이 아닌 상품을 조회했을 경우 예외를 던집니다.
     */

    public SellerOrdersDTO getSellerOrders(Long productId,Seller seller){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("존재하지 않는 제품입니다."));
        if(product.getSeller().getId()!=seller.getId()){
            throw new BadRequestException("자신이 등록한 제품이 아닙니다");
        }
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product);
        Integer completeOrderCount=0,inProgressOrderCount=0;
        for(Order order:orderList){
            if(order.checkStatus()==true){
                completeOrderCount++;
            }else if(order.checkStatus()==false){
                inProgressOrderCount++;
            }
        }
        return SellerOrdersDTO.toEntity(product,orderList,completeOrderCount,inProgressOrderCount);
    }
}
