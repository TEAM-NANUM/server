package server.nanum.repository.querydsl;
import server.nanum.domain.product.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> getProductsByQueryParameters(Long category, Long subcategory, String q, String sort, Integer limit);
}
