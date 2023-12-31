package server.nanum.domain;


import jakarta.persistence.*;
import lombok.*;
import server.nanum.domain.product.Product;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "product_cnt")
    private Integer productCount;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Cart createEmptyCartItem(User user, Product product) {
        return Cart.builder()
                .user(user)
                .product(product)
                .productCount(0)
                .build();
    }

    public void increaseProductCount(Integer quantity) {
        this.productCount += quantity;
    }
}

