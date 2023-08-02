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
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())
                .orElseThrow(()-> new RuntimeException());
        Product product = dto.toEntity(seller,subCategory);
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
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException());
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product);
        return SellerOrdersDTO.toEntity(product,orderList);
    }
}
