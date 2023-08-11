package server.nanum.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.nanum.domain.product.Carousel;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class ProductDTO {
    @Builder
    @Getter
    public static class CategoryItem {
        private Long id;
        private String name;

        public static CategoryItem toDTO(Category category) {
            return ProductDTO.CategoryItem.builder()
                    .id(category.getId())
                    .name(category.getName()).build();
        }

        public static CategoryItem toDTO(SubCategory category) {
            return ProductDTO.CategoryItem.builder()
                    .id(category.getId())
                    .name(category.getName()).build();
        }
    }

    @Builder
    @Getter
    public static class CarouselItem {
        private Long id;
        private String name;
        private String imgUrl;

        public static CarouselItem toDTO(Carousel carousel) {
            return ProductDTO.CarouselItem.builder()
                    .id(carousel.getId())
                    .name(carousel.getName())
                    .imgUrl(carousel.getImgUrl()).build();
        }
    }

    @Builder
    @Getter
    public static class CarouselList {
        private List<CarouselItem> products;

        public static CarouselList toDTO(List<CarouselItem> carouselItems) {
            return ProductDTO.CarouselList.builder()
                    .products(carouselItems)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class CategoryList {
        private List<CategoryItem> categories;

        public static CategoryList toDTO(List<CategoryItem> categoryItems) {
            return builder()
                    .categories(categoryItems)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class SubCategoryList {
        private List<CategoryItem> subcategories;

        public static SubCategoryList toDTO(List<CategoryItem> categoryItems) {
            return builder()
                    .subcategories(categoryItems)
                    .build();
        }
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

        public static ProductListItem toDTO(Product product) {
            return ProductDTO.ProductListItem.builder()
                    .id(product.getId())
                    .imgUrl(product.getImgUrl())
                    .seller(product.getSeller().getName())
                    .deliveryType(product.getDeliveryType().name())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ProductList {
        private Long count;
        private List<ProductListItem> products;

        public static ProductList toDTO(List<ProductListItem> productItems) {
            return ProductList.builder()
                    .count((long) productItems.size())
                    .products(productItems)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ProductDetail {
        private String imgUrl;
        private String seller;
        private String name;
        private String unit;
        private double rating;
        private Integer price;
        private String description;

        public static ProductDetail toDTO(Product product, String sellerNameWithAddress) {
            return ProductDTO.ProductDetail.builder()
                    .imgUrl(product.getImgUrl())
                    .seller(sellerNameWithAddress)
                    .name(product.getName())
                    .unit(product.getUnit()+" kg")
                    .rating(product.getRatingAvg())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .build();
        }
    }

}
