package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.MapKeyCompositeType;
import server.nanum.domain.product.Carousel;
import server.nanum.domain.product.Category;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class ProductDTO {
    @Builder
    @Getter
    @JsonPropertyOrder({"id","name"})
    public static class CategoryItem {
        @Schema(example = "1",description = "카테고리 번호")
        private Long id;
        @Schema(example = "제철과일",description = "카테고리명")
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
    @JsonPropertyOrder({"id","name","imgUrl"})
    public static class CarouselItem {
        @Schema(example = "1",description = "캐러셀 번호")
        private Long id;
        @Schema(example = "Image 1",description = "캐러셀 이름")
        private String name;
        @Schema(description = "캐러셀 이미지 주소")
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
        @Schema(description = "캐러셀 정보")
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
        @Schema(description = "카테고리 정보")
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
        @Schema(description = "세부 카테고리 정보")
        private List<CategoryItem> subcategories;

        public static SubCategoryList toDTO(List<CategoryItem> categoryItems) {
            return builder()
                    .subcategories(categoryItems)
                    .build();
        }
    }

    @Builder
    @Getter
    @JsonPropertyOrder({"id","name","imgUrl","seller","price","deliveryType"})
    public static class ProductListItem {
        @Schema(example = "1",description = "상품 번호")
        private Long id;
        @Schema(description = "상품 대표 이미지" )
        private String imgUrl;
        @Schema(example = "나눔이",description = "상품 판매자명")
        private String seller;
        @Schema(example = "PACKAGE",description = "상품 배송 방법")
        private String deliveryType;
        @Schema(example = "토마토",description = "상품 이름")
        private String name;
        @Schema(example = "1000",description = "상품 가격")
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
    @JsonPropertyOrder({"count","products"})
    public static class ProductList {
        @Schema(example = "1",description = "조회된 상품 개수")
        private Long count;
        @Schema(description = "상품 정보")
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
    @JsonPropertyOrder({"name","seller","imgUrl","description","unit","price","rating"})
    public static class ProductDetail {
        @Schema(description = "상품 대표 이미지" )
        private String imgUrl;
        @Schema(example = "나눔이",description = "상품 판매자명")
        private String seller;
        @Schema(example = "토마토",description = "상품 이름")
        private String name;
        @Schema(example = "10",description = "상품 단위(Kg)")
        private String unit;
        @Schema(example = "5.0",description = "리뷰 평균 별점")
        private double rating;
        @Schema(example = "1000",description = "상품 가격")
        private Integer price;
        @Schema(description = "상품 설명")
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
