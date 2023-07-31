package server.nanum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.nanum.domain.product.Carousel;

@Repository
public interface CarouselRepository extends JpaRepository<Carousel, Long> {

}
