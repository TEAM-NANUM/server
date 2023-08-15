package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.response.CartResponseDTO;
import server.nanum.dto.response.ProductDTO;
import server.nanum.service.ProductService;

/**
 * 상품 관련 컨트롤러
 * 상품 정보와 관련된 API를 제공합니다.
 *
 * @author Jinyenong Seol
 * @version 1.0.0
 * @since 2023-08-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "상품 관련 API", description = "상품 정보를 관리하는 API를 제공합니다.")
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    /**
     * 캐러셀 목록을 가져오는 API
     *
     * @return 캐러셀용 상품 목록
     */
    @Operation(summary = "캐러셀 목록을 가져오는 API", description = "캐러셀 목록을 가져오는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = ProductDTO.CarouselList.class)))
    })
    @GetMapping("/carousel")
    public ResponseEntity<ProductDTO.CarouselList> getCarouselProducts() {
        ProductDTO.CarouselList carouselProducts = productService.getCarouselProducts();
        return ResponseEntity.ok(carouselProducts);
    }

    /**
     * 1차 카테고리 목록을 가져오는 API
     *
     * @return 모든 카테고리 목록
     */
    @Operation(summary = "1차 카테고리 목록을 가져오는 API", description = "1차 카테고리 목록을 가져오는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = ProductDTO.CategoryList.class)))
    })
    @GetMapping("/categories")
    public ResponseEntity<ProductDTO.CategoryList> getCategories() {
        ProductDTO.CategoryList categoryList = productService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    /**
     * 특정 카테고리의 하위 카테고리(2차) 목록을 가져오는 API
     *
     * @param categoryId 1차 카테고리 ID
     * @return 특정 카테고리의 하위 카테고리 목록
     */
    @Operation(summary = "하위 카테고리(2차) 목록을 가져오는 API", description = "특정 카테고리의 하위 카테고리(2차) 목록을 가져오는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = ProductDTO.SubCategoryList.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 1차 카테고리", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/categories/{category_id}/subcategories")
    public ResponseEntity<ProductDTO.SubCategoryList> getSubCategories(@PathVariable("category_id") Long categoryId) {
        ProductDTO.SubCategoryList subCategoryList = productService.getSubCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subCategoryList);
    }

    /**
     * 쿼리 파라미터를 기반으로 상품 목록을 가져오는 API
     *
     * @param subcategory 하위 카테고리 ID (선택 사항)
     * @param q 검색어 (선택 사항)
     * @param sort 정렬 기준 (기본값: "recent") ("recent", "review", "recent", "rating")
     * @param limit 결과 개수 제한 (선택 사항)
     * @return 조건에 맞는 상품 목록
     */
    @Operation(summary = "상품 목록을 가져오는 API", description = "쿼리 파라미터를 기반으로 상품 목록을 가져오는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = ProductDTO.ProductList.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 클라이언트측 입력 (sort 관련해서 문제가 있을 수 있음)", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/products")
    public ResponseEntity<ProductDTO.ProductList> getProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long subcategory,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(required = false) Integer limit
    ) {
        ProductDTO.ProductList productList =
                productService.getProductsByQueryParameters(category, subcategory, q, sort, limit);

        return ResponseEntity.ok(productList);
    }

    /**
     * 특정 상품의 상세 정보를 가져오는 API
     *
     * @param productId 상품 ID
     * @return 상품의 상세 정보
     */
    @Operation(summary = "상품의 상세 정보를 가져오는 API", description = "특정 상품의 상세 정보를 가져오는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공!", content = @Content(mediaType = "application/json",schema = @Schema(implementation = ProductDTO.ProductDetail.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상품", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping("/products/{product_id}")
    public ResponseEntity<ProductDTO.ProductDetail> getProductDetails(@PathVariable("product_id") Long productId) {
        ProductDTO.ProductDetail productDetail = productService.getProductDetailById(productId);
        return ResponseEntity.ok(productDetail);
    }
}
