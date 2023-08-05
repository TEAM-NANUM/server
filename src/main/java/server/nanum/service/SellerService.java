package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;
import server.nanum.domain.Seller;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddProductDTO;
import server.nanum.dto.response.SellerInfoDTO;
import server.nanum.dto.response.SellerOrdersDTO;
import server.nanum.dto.response.SellerProductsDTO;
import server.nanum.repository.OrderRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.SellerRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SellerService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    public void createProduct(Seller seller, AddProductDTO dto){ //제품 등록
        //TODO: 404 에러 처리
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())  //dto에서 서브 카테고리 pk로 서브 카테고리 가져오기
                .orElseThrow(()-> new RuntimeException("404"));
        Product product = dto.toEntity(seller,subCategory);
        //TODO: 409 에러 처리(이미 있는 상품)
        if(productRepository.existsByName(product.getName())){ //프로젝트 중복 여부 -> 이름으로 체크하게 함
            throw new RuntimeException("409");
        }
        productRepository.save(product);
    }
    public SellerInfoDTO getSellerInfo(Seller seller){ //판매자 정보 가져오기
        return new SellerInfoDTO(seller.getName(),seller.getPoint());
    }
    public SellerProductsDTO getSellerProducts(Seller seller){ //판매자의 판매 제품 목록 가져오기
         List<Product> productList = productRepository.findAllBySellerOrderByCreateAt(seller);
         return SellerProductsDTO.toEntity(productList);
    }

    public SellerOrdersDTO getSellerOrders(Long productId){ //판매자의 한 판매 제품의 주문 정보 가져오기
        //TODO: 404 에러 처리
        Product product = productRepository.findById(productId) //제품 pk로 제품 찾기
                .orElseThrow(()-> new RuntimeException("404"));
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product); //제품으로 주문 정보 모두 찾기
        return SellerOrdersDTO.toEntity(product,orderList);
    }
}
