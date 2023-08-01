package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.request.AddReviewDto;
import server.nanum.dto.response.MyReviewOrdersDto;
import server.nanum.dto.response.MyUnReviewOrdersDto;
import server.nanum.dto.response.ProductDTO;
import server.nanum.service.ProductService;
import server.nanum.service.ReviewService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products/carousel")
    public ResponseEntity<ProductDTO.CarouselList> getCarouselProducts() {
        ProductDTO.CarouselList carouselProducts = productService.getCarouselProducts();
        return ResponseEntity.ok(carouselProducts);
    }

    @GetMapping("/categories")
    public ResponseEntity<ProductDTO.CategoryList> getCategories() {
        ProductDTO.CategoryList categoryList = productService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/categories/{category_id}/subcategories")
    public ResponseEntity<ProductDTO.SubCategoryList> getSubCategories(@PathVariable("category_id") Long categoryId) {
        ProductDTO.SubCategoryList subCategoryList = productService.getSubCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subCategoryList);
    }
}
