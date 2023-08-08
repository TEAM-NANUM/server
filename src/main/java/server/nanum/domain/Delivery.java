package server.nanum.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import server.nanum.dto.delivery.DeliveryRequestDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Embedded
    private Address address;

    @Column(name = "is_default", columnDefinition = "boolean default true")
    private boolean isDefault;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeDefaultStatus(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void updateDelivery(String nickname, String phoneNumber, Address address) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

