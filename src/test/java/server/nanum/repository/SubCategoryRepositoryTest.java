package server.nanum.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.SubCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
class SubCategoryRepositoryTest {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("특정 1차 카테고리 ID에 해당하는 모든 2차 카테고리를 조회할 수 있다.")
    void testFindByCategoryId() {
        Category category = Category.builder().name("Category 1").build();
        categoryRepository.save(category);

        SubCategory subCategory1 = SubCategory.builder().name("Subcategory 1").category(category).build();
        SubCategory subCategory2 = SubCategory.builder().name("Subcategory 2").category(category).build();
        subCategoryRepository.save(subCategory1);
        subCategoryRepository.save(subCategory2);

        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(category.getId());

        assertAll(
                () -> assertEquals(2, subCategories.size(), () -> "2차 카테고리의 총 개수는 2개여야 합니다."),
                () -> assertEquals("Subcategory 1", subCategories.get(0).getName(), () -> "첫 번째 2차 카테고리 이름은 Subcategory 1이어야 합니다."),
                () -> assertEquals("Subcategory 2", subCategories.get(1).getName(), () -> "두 번째 2차 카테고리 이름은 Subcategory 2이어야 합니다.")
        );
    }
}