package server.nanum.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.product.*;
import server.nanum.dto.response.ProductDTO;
import server.nanum.repository.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final JPAQueryFactory queryFactory;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CarouselRepository carouselRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO.CarouselList getCarouselProducts() {
        List<Carousel> result = carouselRepository.findAll();

        List<ProductDTO.CarouselItem> carouselItems = result.stream()
                .map(carousel -> ProductDTO.CarouselItem.builder()
                        .id(carousel.getId())
                        .name(carousel.getName())
                        .imgUrl(carousel.getImgUrl()).build())
                .toList();

        return ProductDTO.CarouselList.builder()
                .products(carouselItems)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductDTO.CategoryList getAllCategories() {
        List<Category> result = categoryRepository.findAll();

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(category -> ProductDTO.CategoryItem.builder()
                        .id(category.getId())
                        .name(category.getName()).build())
                .toList();

        return ProductDTO.CategoryList.builder()
                .categories(categoryItems)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductDTO.SubCategoryList getSubCategoriesByCategoryId(Long categoryId) {

        if(categoryRepository.findById(categoryId).orElse(null) == null) {
            // TODO: 1차 카테고리에 대한 하위 카테고리를 찾지 못한 경우에 대한 예외 처리 (예외 클래스 변경 필요)
            throw new RuntimeException("NotFound");
        }

        List<SubCategory> result = subCategoryRepository.findByCategoryId(categoryId);

        List<ProductDTO.CategoryItem> categoryItems = result.stream()
                .map(category -> ProductDTO.CategoryItem.builder()
                        .id(category.getId())
                        .name(category.getName()).build())
                .toList();

        return ProductDTO.SubCategoryList.builder()
                .subcategories(categoryItems)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductDTO.ProductList getProductsByQueryParameters(
            Long subcategory, String q, String sort, Integer limit
    ) {
        QProduct qProduct = QProduct.product;

        // Start building the query
        JPAQuery<Product> query = queryFactory.selectFrom(qProduct);

        if (subcategory != null) {
            query = query.where(qProduct.subCategory.id.eq(subcategory));
        }

        if (q != null && !q.isBlank()) {
            String searchKeyword = "%" + q.trim() + "%";
            query = query.where(
                    qProduct.name.toLowerCase().like(searchKeyword.toLowerCase())
                            .or(qProduct.description.toLowerCase().like(searchKeyword.toLowerCase()))
            );
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "popular":
                    query = query.orderBy(qProduct.purchaseCnt.desc());
                    break;
                case "review":
                    query = query.orderBy(qProduct.reviewCnt.desc());
                    break;
                case "recent":
                    query = query.orderBy(qProduct.createAt.desc());
                    break;
                case "rating":
                    query = query.orderBy(qProduct.ratingAvg.desc());
                    break;
                default:
                    // TODO: 400 애러 발생
                    throw new RuntimeException("지원하지 않는 정렬");
            }
        }

        // Apply limit if provided
        if (limit != null && limit > 0) {
            query = query.limit(limit);
        }

        List<Product> products = query.fetch();

        // Entity ->  DTO
        List<ProductDTO.ProductListItem> productItems = products.stream()
                .map(product -> ProductDTO.ProductListItem.builder()
                        .id(product.getId())
                        .imgUrl(product.getImgUrl())
                        .seller(product.getSeller().getName())
                        .deliveryType(product.getDeliveryType().name())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build())
                .toList();

        return ProductDTO.ProductList.builder()
                .count((long) productItems.size())
                .products(productItems)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductDTO.ProductDetail getProductDetailById(Long productId) {
        Product product = productRepository.findById(productId)        // TODO: 404 예외처리
                .orElseThrow(() -> new RuntimeException());

        // 주소 정보 토큰화
        String[] tokenizedCityAddress;
        tokenizedCityAddress = product.getSeller().getAddress().getDefaultAddress().split(" ");
        // seller명 생성
        String sellerNameWithAddress = tokenizedCityAddress[0] + " " + tokenizedCityAddress[1] + " " + product.getSeller().getName();

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