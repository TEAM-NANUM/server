package server.nanum.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "carousel")
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carousel_id")
    private Long carouselId;

    @Column(name = "name")
    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    // Getters and Setters
}
