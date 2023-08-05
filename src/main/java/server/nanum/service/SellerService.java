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
    public void createProduct(Seller seller, AddProductDTO dto){
        //TODO: 404 에러 처리
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())
                .orElseThrow(()-> new RuntimeException("404"));
        Product product = dto.toEntity(seller,subCategory);
        //TODO: 409 에러 처리(이미 있는 상품)
        if(productRepository.existsByName(product.getName())){
            throw new RuntimeException("409");
        }
        productRepository.save(product);
    }
    public SellerInfoDTO getSellerInfo(Seller seller){
        return new SellerInfoDTO(seller.getName(),seller.getPoint());
    }
    public SellerProductsDTO getSellerProducts(Seller seller){
         List<Product> productList = productRepository.findAllBySellerOrderByCreateAt(seller);
         return SellerProductsDTO.toEntity(productList);
    }

    public SellerOrdersDTO getSellerOrders(Long productId){
        //TODO: 404 에러 처리
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("404"));
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product);
        return SellerOrdersDTO.toEntity(product,orderList);
    }
}
