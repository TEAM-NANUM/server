package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.product.Carousel;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.response.ProductDTO;
import server.nanum.repository.CarouselRepository;
import server.nanum.repository.CategoryRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final CarouselRepository carouselRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO.CarouselList getCarouselProducts() {
        List<Carousel> result = carouselRepository.findAll();

        List<ProductDTO.CarouselItem> carouselItems = result.stream()
                .map(carousel -> ProductDTO.CarouselItem.builder()
                        .id(carousel.getId())
                        .name(carousel.getName())
                        .imgUrl(carousel.getImgUrl()).build())
                .toList();

        return ProductDTO.CarouselList.builder()
                .products(carouselItems)
                .build();
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public ProductDTO.SubCategoryList getSubCategoriesByCategoryId(Long categoryId) {

        if(categoryRepository.findById(categoryId).orElse(null) == null) {
            // TODO: 1차 카테고리에 대한 하위 카테고리를 찾지 못한 경우에 대한 예외 처리 (예외 클래스 변경 필요)
            throw new RuntimeException("NotFound");
        }

        List<SubCategory> result = subCategoryRepository.findByCategoryId(categoryId);

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