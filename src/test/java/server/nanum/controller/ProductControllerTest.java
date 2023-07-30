package server.nanum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import server.nanum.dto.response.ProductDTO;
import server.nanum.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private List<ProductDTO.CategoryItem> categoryItems;

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