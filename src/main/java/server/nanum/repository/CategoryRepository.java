package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.nanum.domain.product.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
