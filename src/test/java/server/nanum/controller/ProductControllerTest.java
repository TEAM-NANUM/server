package server.nanum.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import server.nanum.config.SecurityConfig;
import server.nanum.dto.response.ProductDTO;
import server.nanum.filter.JwtAuthenticationFilter;
import server.nanum.service.ProductService;
import server.nanum.utils.JwtProvider;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Disabled
@ActiveProfiles("test")
@WithAnonymousUser
@WebMvcTest(controllers = ProductController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class), // Exclude JwtProvider
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class), // Exclude JwtProvider
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private List<ProductDTO.CategoryItem> categoryItems;
    private List<ProductDTO.CarouselItem> carouselItems;

    @Nested
    @DisplayName("캐러셀 테스트")
    class CarouselTest {
        @BeforeEach
        void setUp() {
            carouselItems = new ArrayList<>();
            carouselItems.add(ProductDTO.CarouselItem.builder().id(1L).name("Carousel 1").imgUrl("S3 이미지 링크 1").build());
            carouselItems.add(ProductDTO.CarouselItem.builder().id(2L).name("Carousel 2").imgUrl("S3 이미지 링크 2").build());
        }

        @Test
        @DisplayName("캐러셀 상품 목록을 가져올 수 있다.")
        void testGetCarouselProducts() throws Exception {
            ProductDTO.CarouselList carouselList = ProductDTO.CarouselList.builder()
                    .products(carouselItems)
                    .build();

            when(productService.getCarouselProducts()).thenReturn(carouselList);

            mockMvc.perform(get("/api/products/carousel"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.products.length()").value(carouselItems.size()))
                    .andExpect(jsonPath("$.products[0].id").value(carouselItems.get(0).getId()))
                    .andExpect(jsonPath("$.products[0].name").value(carouselItems.get(0).getName()))
                    .andExpect(jsonPath("$.products[0].imgUrl").value(carouselItems.get(0).getImgUrl()))
                    .andExpect(jsonPath("$.products[1].id").value(carouselItems.get(1).getId()))
                    .andExpect(jsonPath("$.products[1].name").value(carouselItems.get(1).getName()))
                    .andExpect(jsonPath("$.products[1].imgUrl").value(carouselItems.get(1).getImgUrl()));
        }
    }

    @Nested
    @DisplayName("카테고리 테스트")
    class CategoryTest {
        @BeforeEach
        void setUp() {
            categoryItems = new ArrayList<>();
            categoryItems.add(ProductDTO.CategoryItem.builder().id(1L).name("Category 1").build());
            categoryItems.add(ProductDTO.CategoryItem.builder().id(2L).name("Category 2").build());
        }

        @Test
        @DisplayName("1차 카테고리 목록을 가져올 수 있다.")
        void testGetCategories() throws Exception {
            ProductDTO.CategoryList categoryList = ProductDTO.CategoryList.builder()
                    .categories(categoryItems)
                    .build();
            System.out.println(categoryList);
            when(productService.getAllCategories()).thenReturn(categoryList);

            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.categories.length()").value(categoryItems.size()))
                    .andExpect(jsonPath("$.categories[0].id").value(categoryItems.get(0).getId()))
                    .andExpect(jsonPath("$.categories[0].name").value(categoryItems.get(0).getName()))
                    .andExpect(jsonPath("$.categories[1].id").value(categoryItems.get(1).getId()))
                    .andExpect(jsonPath("$.categories[1].name").value(categoryItems.get(1).getName()));
        }

        @Test
        @DisplayName("특정 1차 카테고리에 속하는 2차 카테고리 목록을 가져올 수 있다.")
        void testGetSubCategories() throws Exception {
            Long categoryId = 1L;

            ProductDTO.SubCategoryList subCategoryList = ProductDTO.SubCategoryList.builder()
                    .subcategories(categoryItems)
                    .build();

            when(productService.getSubCategoriesByCategoryId(categoryId)).thenReturn(subCategoryList);

            mockMvc.perform(get("/api/categories/{category_id}/subcategories", categoryId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.subcategories.length()").value(categoryItems.size()))
                    .andExpect(jsonPath("$.subcategories[0].id").value(categoryItems.get(0).getId()))
                    .andExpect(jsonPath("$.subcategories[0].name").value(categoryItems.get(0).getName()))
                    .andExpect(jsonPath("$.subcategories[1].id").value(categoryItems.get(1).getId()))
                    .andExpect(jsonPath("$.subcategories[1].name").value(categoryItems.get(1).getName()));
        }
    }
}