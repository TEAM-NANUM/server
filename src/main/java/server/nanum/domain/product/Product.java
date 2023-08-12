package server.nanum.domain.product;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.Seller;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "unit")
    private Integer unit;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "rating_avg")
    private Float ratingAvg;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToOne
    @JoinColumn(name = "carousel_id")
    private Carousel carousel;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @Column(name = "review_cnt")
    private Integer reviewCnt;

    @Column(name = "purchase_cnt")
    private Integer purchaseCnt;

    public void setViewCnt(Integer viewCnt) {
        this.viewCnt = viewCnt;
    }

    public void setReviewCnt(Integer reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public void setPurchaseCnt(Integer purchaseCnt) {
        this.purchaseCnt = purchaseCnt;
    }

    public void setRatingAvg(Float ratingAvg) {
        this.ratingAvg = ratingAvg;
    }
}

