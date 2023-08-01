package server.nanum.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller")
@Getter
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

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "default_address")
    private String defaultAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "point")
    private Long point;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    // Getters and Setters
}

