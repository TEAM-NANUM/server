package server.nanum.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.domain.product.Carousel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
class CarouselRepositoryTest {

    @Autowired
    private CarouselRepository carouselRepository;

    Carousel carousel1;
    Carousel carousel2;

    @BeforeEach
    public void setUp() {
        carousel1 = Carousel.builder().name("Carousel 1").imgUrl("Carousel 1 Url").build();
        carousel2 = Carousel.builder().name("Carousel 2").imgUrl("Carousel 2 Url").build();

        carouselRepository.save(carousel1);
        carouselRepository.save(carousel2);
    }

    @Test
    @DisplayName("캐러셀 목록 전체를 조회할 수 있다.")
    void testSaveAndFindAll() {
        List<Carousel> carousels = carouselRepository.findAll();


        assertAll(
                () -> assertEquals(2, carousels.size(), () -> "캐러셀 항목의 총 개수는 2 개여야 합니다."),
                () -> assertEquals("Carousel 1", carousels.get(0).getName(), () -> "첫 번째 캐러셀 항목 이름은 Carousel 1 이어야 합니다."),
                () -> assertEquals("Carousel 1 Url", carousels.get(0).getImgUrl(), () -> "첫 번째 캐러셀 항목 Url은 Carousel 1 Url 이어야 합니다."),
                () -> assertEquals("Carousel 2", carousels.get(1).getName(), () -> "두 번째 캐러셀 항목 이름은 Carousel 2 이어야 합니다."),
                () -> assertEquals("Carousel 2 Url", carousels.get(1).getImgUrl(), () -> "두 번째 캐러셀 항목 Url은 Carousel 2 Url 이어야 합니다.")
        );
    }
}