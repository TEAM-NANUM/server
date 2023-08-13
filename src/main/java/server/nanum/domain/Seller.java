package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Embedded
    private Address address;

    @Column(name = "point")
    private Long point;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    public void withPoint(long point) {
        this.point = point;
    }

    public void withEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}

