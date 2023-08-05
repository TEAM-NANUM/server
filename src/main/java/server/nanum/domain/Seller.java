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

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
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

    public Seller withPoint(long point) {
        // 판매자 포인트 변경
        this.point = point;
        return this;
    }

    public Seller withEncryptedPassword(String encryptedPassword) {
        // 암호화된 판매자 비밀번호 설정
        this.password = encryptedPassword;
        return this;
    }
}

