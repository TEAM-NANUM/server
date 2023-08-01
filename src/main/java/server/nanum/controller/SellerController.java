package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.request.AddProductDto;
import server.nanum.dto.response.SellerInfoDto;
import server.nanum.dto.response.SellerOrdersDto;
import server.nanum.dto.response.SellerProductsDto;
import server.nanum.service.SellerService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seller")
public class SellerController {
    private final SellerService sellerService;
    @GetMapping()
    public ResponseEntity<SellerInfoDto> getSellerInfo(){
        Long sellerId=1L;
        SellerInfoDto dto = sellerService.getSellerInfo(sellerId);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/products")
    public ResponseEntity<SellerProductsDto> getSellerProducts(){
        Long sellerId=1L;
        SellerProductsDto dto = sellerService.getSellerProducts(sellerId);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping("/product")
    public ResponseEntity<Void> addProduct(@RequestBody AddProductDto dto){
        Long sellerId=1L;
        sellerService.createProduct(sellerId,dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{product_id}")
    public ResponseEntity<SellerOrdersDto> getSellerOrders(@PathVariable("product_id") Long productId){
        SellerOrdersDto dto = sellerService.getSellerOrders(productId);
        return ResponseEntity.ok().body(dto);
    }
}
