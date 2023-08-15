package server.nanum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.exception.BadRequestException;
import server.nanum.repository.querydsl.ProductRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductRepositoryCustomImplTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long categoryId;
    private Long subcategoryId;

    @BeforeEach
    void setUp() {
        Category category = Category.builder()
                .name("카테고리")
                .build();

        SubCategory subCategory = SubCategory.builder()
                .name("서브카테고리")
                .category(category)
                .build();

        Product product = Product.builder()
                .name("상품")
                .price(100)
                .unit(1)
                .imgUrl("url")
                .description("설명")
                .deliveryType(DeliveryType.DIRECT)
                .ratingAvg(5.0f)
                .createAt(LocalDateTime.now())
                .subCategory(subCategory)
                .viewCnt(10)
                .reviewCnt(5)
                .purchaseCnt(3)
                .build();

        categoryRepository.save(category);
        subCategoryRepository.save(subCategory);
        productRepository.save(product);

        categoryId = category.getId();
        subcategoryId = subCategory.getId();
    }


    @Test
    void testGetProductsByCategory() {
        List<Product> products = productRepository.getProductsByQueryParameters(categoryId, null, null, "recent", 10);
        assertThat(products).isNotEmpty();
    }

    @Test
    void testGetProductsBySubCategory() {
        List<Product> products = productRepository.getProductsByQueryParameters(null, subcategoryId, null, "recent", 10);
        assertThat(products).isNotEmpty();
    }

    @Test
    void testBothCategoryAndSubCategory() {
        assertThatThrownBy(() -> productRepository.getProductsByQueryParameters(categoryId, subcategoryId, null, "recent", 10))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("category와 subcategory 중 하나만 적용할 수 있습니다.");
    }
}
