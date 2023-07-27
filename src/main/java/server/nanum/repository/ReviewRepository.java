package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.nanum.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

}
