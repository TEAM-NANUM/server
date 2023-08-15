package server.nanum.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import server.nanum.domain.product.Product;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "product_cnt")
    private Integer productCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "order")
    private Review review;

    public void setReview(Review review){
        this.review=review;
    }
    public boolean checkStatus(){
        if(this.deliveryStatus==DeliveryStatus.DELIVERED){
            return true;
        }else{
            return false;
        }
    }
    public Float getRating(){
        return this.review.getRating();
    }
}

