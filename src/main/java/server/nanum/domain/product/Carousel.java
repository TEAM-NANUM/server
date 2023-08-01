package server.nanum.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "carousel")
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carousel_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_url")
    private String imgUrl;
}
