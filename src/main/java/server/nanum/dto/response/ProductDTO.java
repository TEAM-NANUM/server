package server.nanum.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class ProductDTO {
    @Builder
    @Getter
    public static class CategoryItem {
        private Long id;
        private String name;
    }

    @Builder
    @Getter
    public static class CarouselItem {
        private Long id;
        private String name;
        private String imgUrl;
    }

    @Builder
    @Getter
    public static class CarouselList {
        private List<CarouselItem> products;
    }

    @Builder
    @Getter
    public static class CategoryList {
        private List<CategoryItem> categories;
    }

    @Builder
    @Getter
    public static class SubCategoryList {
        private List<CategoryItem> subcategories;
    }

    @Builder
    @Getter
    public static class ProductListItem {
        private Long id;
        private String imgUrl;
        private String seller;
        private String deliveryType;
        private String name;
        private Integer price;
    }

    @Builder
    @Getter
    public static class ProductList {
        private Long count;
        private List<ProductListItem> products;
    }
}
