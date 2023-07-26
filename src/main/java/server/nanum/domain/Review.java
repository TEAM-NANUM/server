package server.nanum.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    // Getters and Setters
}

