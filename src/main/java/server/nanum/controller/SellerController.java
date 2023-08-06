package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.Seller;
import server.nanum.dto.request.AddProductDTO;
import server.nanum.dto.response.*;
import server.nanum.service.SellerService;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name="판매자 관련 API",description = "판매자 관련 API입니다. 판매자 정보 조회, 판매 상품 등록, 판매자가 등록한 상품 조회, 상품에 등록된 주문 조회를 수행합니다.")
@RequestMapping("/api/seller")
@PreAuthorize("hasRole('ROLE_SELLER')")
public class SellerController {
    private final SellerService sellerService;
    @Operation(summary = "판매자 정보 조회 API", description = "판매자 등록 상품 조회 페이지에서 판매자의 정보를 가져오는 API입니다. (API명세서 30번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = SellerInfoDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping()
    public ResponseEntity<SellerInfoDTO> getSellerInfo(@CurrentUser  Seller seller){
        SellerInfoDTO dto = sellerService.getSellerInfo(seller);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "판매자가 등록한 상품 조회 API", description = "판매자가 등록한 상품의 정보를 가져오는 API입니다. (API명세서 31번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = SellerProductsDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/products")
    public ResponseEntity<SellerProductsDTO> getSellerProducts(@CurrentUser Seller seller){
        SellerProductsDTO dto = sellerService.getSellerProducts(seller);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "판매 상품 등록 API", description = "판매자가 상품의 대표 이미지, 배송 종류, 카테고리 ID, 이름, 개당가격, 판매 단위, 설명을 입력해 상품을 추가하는 API입니다. (API명세서 32번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 추가 성공", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "요청에 누락이 있는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "카테고리 ID로 카테고리을 찾을 수 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "같은 이름의 상품이 이미 존재하는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/product")
    public ResponseEntity<Void> addProduct(@CurrentUser Seller seller, @RequestBody AddProductDTO dto){
        sellerService.createProduct(seller,dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품에 등록된 주문 조회 API", description = "상품의 ID로 해당 상품에 등록된 주문을 모두 가져오는 API입니다. (API명세서 33번)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = SellerOrdersDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "상품 ID로 상품을 찾을 수 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{product_id}")
    public ResponseEntity<SellerOrdersDTO> getSellerOrders(
            @Parameter(name="ID",description = "상품의 id",in = ParameterIn.QUERY)
            @PathVariable("product_id") Long productId){
        SellerOrdersDTO dto = sellerService.getSellerOrders(productId);
        return ResponseEntity.ok().body(dto);
    }
}
