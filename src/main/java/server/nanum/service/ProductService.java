package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.product.*;
import server.nanum.repository.CarouselRepository;
import server.nanum.repository.CategoryRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.SubCategoryRepository;
import server.nanum.dto.response.ProductDTO;
import server.nanum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CarouselRepository carouselRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO.CarouselList getCarouselProducts() {
        List<Carousel> result = carouselRepository.findAll();

        List<ProductDTO.CarouselItem> carouselItems = result.stream()
                .map(ProductDTO.CarouselItem::toDTO)
                .toList();

        return ProductDTO.CarouselList.toDTO(carouselItems);
    }

    @Transactional(readOnly = true)
    public ProductDTO.CategoryList getAllCategories() {
        List<Category> result = categoryRepository.findAll();

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(ProductDTO.CategoryItem::toDTO)
                .toList();

        return ProductDTO.CategoryList.toDTO(categoryItems);
    }

    @Transactional(readOnly = true)
    public ProductDTO.SubCategoryList getSubCategoriesByCategoryId(Long categoryId) {

        if(!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("존재하지 않는 1차 카테고리 입니다.");
        }

        List<SubCategory> result = subCategoryRepository.findByCategoryId(categoryId);

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(ProductDTO.CategoryItem::toDTO)
                .toList();

        return ProductDTO.SubCategoryList.toDTO(categoryItems);
    }

    @Transactional(readOnly = true)
    public ProductDTO.ProductList getProductsByQueryParameters(
            Long subcategory, String q, String sort, Integer limit
    ) {
        List<Product> products = productRepository.getProductsByQueryParameters(subcategory, q, sort, limit);

        // Entity ->  DTO
        List<ProductDTO.ProductListItem> productItems = products.stream()
                .map(ProductDTO.ProductListItem::toDTO)
                .toList();

        return ProductDTO.ProductList.toDTO(productItems);
    }

    @Transactional(readOnly = true)
    public ProductDTO.ProductDetail getProductDetailById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        // 주소 정보 토큰화, seller명 생성
        String[] tokenizedCityAddress = product.getSeller().getAddress().getDefaultAddress().split(" ");
        String sellerNameWithAddress = tokenizedCityAddress[0] + " " + tokenizedCityAddress[1] + " " + product.getSeller().getName();

        return ProductDTO.ProductDetail.toDTO(product, sellerNameWithAddress);
    }
}