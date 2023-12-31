package server.nanum.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "receiver")
    private String receiver;

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

    public void resetDefault() {
        this.isDefault = false;
    }

    public void makeDefault() {
        this.isDefault = true;
    }


    public void update(String receiver, String nickname, String phoneNumber, Address address) {
        this.receiver = receiver;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getUsername() {
        return user.getName();
    }

    public String getZipCode() {
        return address.getZipCode();
    }

    public String getDefaultAddress() {
        return address.getDefaultAddress();
    }

    public String getDetailAddress() {
        return address.getDetailAddress();
    }
}

