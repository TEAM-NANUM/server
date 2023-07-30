package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
