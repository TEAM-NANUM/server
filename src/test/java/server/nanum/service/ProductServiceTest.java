package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.SubCategory;
import server.nanum.dto.response.ProductDTO;
import server.nanum.repository.CategoryRepository;
import server.nanum.repository.SubCategoryRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

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

            when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(categoryList.get(0)));
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

            when(categoryRepository.findById(categoryId)).thenReturn(null);

            // TODO: NotFound 예외로 변경 필요
            assertThrows(RuntimeException.class, () -> productService.getSubCategoriesByCategoryId(categoryId));
        }
    }

}