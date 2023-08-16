package server.nanum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import server.nanum.domain.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByOrderProductId(Long productId);
    @Query(value = "SELECT r FROM Review r ORDER BY r.createAt DESC")
    List<Review> findAllByOrderByCreateAtDesc();

}
