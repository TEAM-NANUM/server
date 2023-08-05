package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.Seller;
import server.nanum.dto.request.AddProductDTO;
import server.nanum.dto.response.SellerInfoDTO;
import server.nanum.dto.response.SellerOrdersDTO;
import server.nanum.dto.response.SellerProductsDTO;
import server.nanum.service.SellerService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seller")
public class SellerController {
    private final SellerService sellerService;
    @GetMapping()
    public ResponseEntity<SellerInfoDTO> getSellerInfo(@CurrentUser  Seller seller){
        SellerInfoDTO dto = sellerService.getSellerInfo(seller);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/products")
    public ResponseEntity<SellerProductsDTO> getSellerProducts(Seller seller){
        SellerProductsDTO dto = sellerService.getSellerProducts(seller);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping("/product")
    public ResponseEntity<Void> addProduct(Seller seller, @RequestBody AddProductDTO dto){
        sellerService.createProduct(seller,dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{product_id}")
    public ResponseEntity<SellerOrdersDTO> getSellerOrders(@PathVariable("product_id") Long productId){
        SellerOrdersDTO dto = sellerService.getSellerOrders(productId);
        return ResponseEntity.ok().body(dto);
    }
}
