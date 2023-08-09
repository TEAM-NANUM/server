package server.nanum.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.product.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    Category category1;
    Category category2;

    @BeforeEach
    public void setUp() {
        category1 = Category.builder().name("Category 1").build();
        category2 = Category.builder().name("Category 2").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @Test
    @DisplayName("1차 카테고리 전체를 조회할 수 있다.")
    void testSaveAndFindAll() {
        List<Category> categories = categoryRepository.findAll();

        assertAll(
                () -> assertEquals(2, categories.size(), () -> "1차 카테고리의 총 개수는 2 개여야 합니다."),
                () -> assertEquals("Category 1", categories.get(0).getName(), () -> "첫 번째 1차 카테고리 이름은 Category 1 이어야 합니다."),
                () -> assertEquals("Category 2", categories.get(1).getName(), () -> "두 번째 1차 카테고리 이름은 Category 2 이어야 합니다.")
        );
    }

    @Test
    @DisplayName("Id를 통해 1차 카테고리를 검색할 수 있다.")
    void testFindById() {
        Category foundCategory = categoryRepository.findById(category1.getId()).orElse(null);
        assertAll(
                () -> assertNotNull(foundCategory, () -> "1차 카테고리에서 Category 1이 검색되어야 합니다."),
                () -> {
                    assert foundCategory != null;
                    assertEquals("Category 1", foundCategory.getName(), () -> "1차 카테고리에서 검색된 카테고리의 이름이 Category 1 이어야 합니다.");
                }
        );
    }
}