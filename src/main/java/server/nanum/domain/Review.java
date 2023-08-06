package server.nanum.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Review makeReview(Order order,Float rating, String comment){
        Review review = new Review();
        review.rating=rating;
        review.comment=comment;
        review.order=order;
        return review;
    }
}

