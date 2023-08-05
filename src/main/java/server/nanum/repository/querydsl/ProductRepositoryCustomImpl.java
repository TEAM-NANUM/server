package server.nanum.repository.querydsl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.QProduct;
import server.nanum.exception.BadRequestException;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> getProductsByQueryParameters(Long subcategory, String q, String sort, Integer limit) {
        QProduct qProduct = QProduct.product;
        JPAQuery<Product> query = queryFactory.selectFrom(qProduct);

        applyFilters(query, qProduct, subcategory, q);
        applySorting(query, qProduct, sort);

        if (limit != null && limit > 0) {
            query.limit(limit);
        }

        return query.fetch();
    }

    private void applyFilters(JPAQuery<Product> query, QProduct qProduct, Long subcategory, String q) {
        if (subcategory != null) {
            query.where(qProduct.subCategory.id.eq(subcategory));
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
