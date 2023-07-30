package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.response.ProductDTO;
import server.nanum.repository.CategoryRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public ProductDTO.CategoryList getAllCategories() {
        List<Category> result = categoryRepository.findAll();

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(category -> ProductDTO.CategoryItem.builder()
                        .id(category.getId())
                        .name(category.getName()).build())
                .toList();

        return ProductDTO.CategoryList.builder()
                .categories(categoryItems)
                .build();
    }

    // Method to fetch subcategories by category ID
    public ProductDTO.SubCategoryList getSubCategoriesByCategoryId(Long categoryId) {
        List<SubCategory> result = subCategoryRepository.findByCategoryId(categoryId);

        if (result.isEmpty()) {
            // TODO: 1차 카테고리에 대한 하위 카테고리를 찾지 못한 경우에 대한 예외 처리
            throw new RuntimeException("NotFound");
        }

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(category -> ProductDTO.CategoryItem.builder()
                        .id(category.getId())
                        .name(category.getName()).build())
                .toList();

        return ProductDTO.SubCategoryList.builder()
                .subcategories(categoryItems)
                .build();
    }
}