package server.nanum.repository.querydsl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.QProduct;
import server.nanum.domain.product.QSubCategory;
import server.nanum.exception.BadRequestException;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> getProductsByQueryParameters(Long category, Long subcategory, String q, String sort, Integer limit) {
        QProduct qProduct = QProduct.product;
        JPAQuery<Product> query = queryFactory.selectFrom(qProduct);

        // 1, 2번 조건 : category와 subcategory 둘 중 하나만 필터로 적용 가능
        if (category != null && subcategory != null) {
            throw new BadRequestException("category와 subcategory 중 하나만 적용할 수 있습니다.");
        }

        // 3, 4, 5번 조건 : category를 기반으로 모든 subcategory 상품 조회
        applyFilters(query, qProduct, category, subcategory, q);
        applySorting(query, qProduct, sort);

        if (limit != null && limit > 0) {
            query.limit(limit);
        }

        return query.fetch();
    }

    private void applyFilters(JPAQuery<Product> query, QProduct qProduct, Long category, Long subcategory, String q) {
        if (subcategory != null) {
            query.where(qProduct.subCategory.id.eq(subcategory));
        } else if (category != null) {
            // category에 해당하는 모든 subcategory의 상품을 조회
            QSubCategory qSubCategory = QSubCategory.subCategory;
            JPAQuery<Long> subCategories = queryFactory.select(qSubCategory.id).from(qSubCategory)
                    .where(qSubCategory.category.id.eq(category));
            query.where(qProduct.subCategory.id.in(subCategories));
        }

        if (q != null && !q.isBlank()) {
            String searchKeyword = "%" + q.trim() + "%";
            query.where(
                    qProduct.name.toLowerCase().like(searchKeyword.toLowerCase())
                            .or(qProduct.description.toLowerCase().like(searchKeyword.toLowerCase()))
            );
        }
    }

    private void applySorting(JPAQuery<Product> query, QProduct qProduct, String sort) {
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "popular" -> query.orderBy(qProduct.purchaseCnt.desc());
                case "review" -> query.orderBy(qProduct.reviewCnt.desc());
                case "recent" -> query.orderBy(qProduct.createAt.desc());
                case "rating" -> query.orderBy(qProduct.ratingAvg.desc());
                default -> throw new BadRequestException("지원하지 않는 정렬 형식입니다.");
            }
        }
    }
}
