package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.Address;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.Seller;
import server.nanum.domain.product.Carousel;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.response.ProductDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.CarouselRepository;
import server.nanum.repository.CategoryRepository;
import server.nanum.repository.ProductRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    private CarouselRepository carouselRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("캐러셀 테스트")
    class CarouselTest {

        private ArrayList<Carousel> carouselList;

        @BeforeEach
        void innerSetup() {
            carouselList = new ArrayList<>();
            carouselList.add(new Carousel(1L, "Carousel 1", "Carousel 1 Url","returnUrl1"));
            carouselList.add(new Carousel(2L, "Carousel 2", "Carousel 2 Url","returnUrl2"));
            carouselList.add(new Carousel(3L, "Carousel 3", "Carousel 3 Url","returnUrl3"));
        }

        @Test
        @DisplayName("캐러셀 상품 목록을 가져올 수 있다.")
        void testGetCarouselProducts() {
            when(carouselRepository.findAll()).thenReturn(carouselList);

            ProductDTO.CarouselList result = productService.getCarouselProducts();

            assertAll(
                    () -> assertEquals(3, result.getProducts().size(), () -> "캐러셀 상품의 총 개수는 3개여야 한다."),
                    () -> assertEquals("Carousel 1", result.getProducts().get(0).getName(), () -> "첫 번째 캐러셀 상품의 name은 Carousel 1 이어야 한다."),
                    () -> assertEquals("Carousel 1 Url", result.getProducts().get(0).getImgUrl(), () -> "첫 번째 캐러셀 상품의 imgUrl은 Carousel 1 Url 이어야 한다."),
                    () -> assertEquals("Carousel 2", result.getProducts().get(1).getName(), () -> "두 번째 캐러셀 상품의 name은 Carousel 2 이어야 한다."),
                    () -> assertEquals("Carousel 2 Url", result.getProducts().get(1).getImgUrl(), () -> "두 번째 캐러셀 상품의 imgUrl은 Carousel 2 Url 이어야 한다."),
                    () -> assertEquals("Carousel 3", result.getProducts().get(2).getName(), () -> "세 번째 캐러셀 상품의 name은 Carousel 3 이어야 한다."),
                    () -> assertEquals("Carousel 3 Url", result.getProducts().get(2).getImgUrl(), () -> "세 번째 캐러셀 상품의 imgUrl은 Carousel 3 Url 이어야 한다.")
            );
        }
    }

    @Nested
    @DisplayName("카테고리 테스트")
    class CategoryTest {

        private ArrayList<Category> categoryList;
        private ArrayList<SubCategory> subCategoryList;

        @BeforeEach
        void innerSetup() {
            categoryList = new ArrayList<>();
            categoryList.add(new Category(1L, "Category 1"));
            categoryList.add(new Category(2L, "Category 2"));

            subCategoryList = new ArrayList<>();
            subCategoryList.add(new SubCategory(1L, "Subcategory 1", categoryList.get(0)));
            subCategoryList.add(new SubCategory(2L, "Subcategory 2", categoryList.get(0)));
        }

        @Test
        @DisplayName("1차 카테고리 목록을 가져올 수 있다.")
        void testGetAllCategories() {
            when(categoryRepository.findAll()).thenReturn(categoryList);

            ProductDTO.CategoryList result = productService.getAllCategories();

            assertAll(
                    () -> assertEquals(2, result.getCategories().size(),()-> "1차 카테고리의 총 개수는 2개 여야 한다."),
                    () -> assertEquals("Category 1", result.getCategories().get(0).getName(), ()-> "첫 번째 1차 카테고리의 name은 Category 1 이어야 한다."),
                    () -> assertEquals("Category 2", result.getCategories().get(1).getName(), ()-> "두 번째 1차 카테고리의 name은 Category 2 이어야 한다.")
            );
        }

        @Test
        @DisplayName("1차 카테고리 Id를 통해 2차 카테고리 목록을 조회할 수 있다.")
        void testGetSubCategoriesByCategoryId() {
            Long categoryId = 1L;

            when(categoryRepository.existsById(categoryId)).thenReturn(true);
            when(subCategoryRepository.findByCategoryId(categoryId)).thenReturn(subCategoryList);

            ProductDTO.SubCategoryList result = productService.getSubCategoriesByCategoryId(categoryId);

            assertAll(
                    () -> assertEquals(2, result.getSubcategories().size(), ()-> "2차 카테고리의 총 개수는 2개 여야 한다."),
                    () -> assertEquals("Subcategory 1", result.getSubcategories().get(0).getName(), ()-> "첫 번째 2차 카테고리의 name은 Subcategory 1 이어야 한다."),
                    () -> assertEquals("Subcategory 2", result.getSubcategories().get(1).getName(), ()-> "두 번째 2차 카테고리의 name은 Subcategory 2 이어야 한다.")
            );
        }

        @Test
        @DisplayName("1차 카테고리 Id가 존재하지 않는 경우 2차 카테고리 목록을 가져오려 할때 NotFound 예외가 발생한다.")
        void testGetSubCategoriesByCategoryIdNotFound() {
            Long categoryId = 99L;
            when(categoryRepository.existsById(categoryId)).thenReturn(false);
            assertThrows(NotFoundException.class, () -> productService.getSubCategoriesByCategoryId(categoryId));
        }
    }

//    @Nested
//    @DisplayName("상품 테스트")
//    class ProductTest {
//        @Test
//        @DisplayName("쿼리 파라미터를 이용하여 상품 목록을 가져올 수 있다.")
//        void testGetProductsByQueryParameters() {
//            List<Product> productList = new ArrayList<>();
//            productList.add(Product.builder().price(100).name("Product 1").deliveryType(DeliveryType.DIRECT).seller(Seller.builder().build()).build());
//            productList.add(Product.builder().price(200).name("Product 2").deliveryType(DeliveryType.PACKAGE).seller(Seller.builder().build()).build());
//
//            Long subcategoryId = 1L;
//            String query = "test";
//            String sort = "recent";
//            Integer limit = 10;
//            when(productRepository.getProductsByQueryParameters(subcategoryId, query, sort, limit)).thenReturn(productList);
//
//            ProductDTO.ProductList result = productService.getProductsByQueryParameters(subcategoryId, query, sort, limit);
//
//            assertAll(
//                    () -> assertEquals(2, result.getProducts().size(), () -> "상품 목록의 총 개수는 2개여야 합니다."),
//                    () -> assertEquals("Product 1", result.getProducts().get(0).getName(), () -> "첫 번째 상품의 이름은 Product 1 이어야 합니다."),
//                    () -> assertEquals(100, result.getProducts().get(0).getPrice(), () -> "첫 번째 상품의 가격은 100 이어야 합니다."),
//                    () -> assertEquals("Product 2", result.getProducts().get(1).getName(), () -> "두 번째 상품의 이름은 Product 2 이어야 합니다."),
//                    () -> assertEquals(200, result.getProducts().get(1).getPrice(), () -> "두 번째 상품의 가격은 200 이어야 합니다.")
//            );
//        }

        @Test
        @DisplayName("상품 ID를 이용하여 상품 상세 정보를 가져올 수 있다.")
        void testGetProductDetailById() {
            Long productId = 1L;

            Seller seller = Seller.builder().name("Seller Name").address(Address.builder().zipCode("zipcode").defaultAddress("City Street").detailAddress("detail").build()).build();
            Product product = Product.builder().id(productId).price(100).name("Product 1").ratingAvg(4.2F).deliveryType(DeliveryType.DIRECT).seller(seller).viewCnt(0).build();

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            ProductDTO.ProductDetail result = productService.getProductDetailById(productId);

            assertAll(
                    () -> assertEquals("Product 1", result.getName(), () -> "상품의 이름은 Test Product 이어야 합니다."),
                    () -> assertEquals(100, result.getPrice(), () -> "상품의 가격은 100 이어야 합니다."),
                    () -> assertEquals("City Street Seller Name", result.getSeller(), () -> "판매자 이름과 주소는 'City Street Seller Name' 이어야 합니다.")
            );
        }
    }

