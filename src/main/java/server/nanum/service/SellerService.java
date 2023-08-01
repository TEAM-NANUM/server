package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;
import server.nanum.domain.Seller;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.request.AddProductDto;
import server.nanum.dto.response.SellerInfoDto;
import server.nanum.dto.response.SellerOrdersDto;
import server.nanum.dto.response.SellerProductsDto;
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
    private final SellerRepository sellerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    public void createProduct(Long sellerId,AddProductDto dto){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()-> new RuntimeException());
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())
                .orElseThrow(()-> new RuntimeException());
        Product product = dto.toEntity(seller,subCategory);
        productRepository.save(product);
    }
    public SellerInfoDto getSellerInfo(Long sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()-> new RuntimeException());
        return new SellerInfoDto(seller.getName(),seller.getPoint());
    }
    public SellerProductsDto getSellerProducts(Long sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()-> new RuntimeException());
         List<Product> productList = productRepository.findAllBySellerOrderByCreateAt(seller);
         return SellerProductsDto.toEntity(productList);
    }
    public SellerOrdersDto getSellerOrders(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException());
        List<Order> orderList = orderRepository.findByProductOrderByCreateAtDesc(product);
        return SellerOrdersDto.toEntity(product,orderList);
    }
}
