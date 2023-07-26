package server.nanum.domain;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "comment")
    private String comment;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Getters and Setters
}

