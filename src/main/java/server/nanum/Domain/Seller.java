package server.nanum.Domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "username")
    private String username;

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

    @Column(name = "create_at")
    private LocalDateTime createAt;

    // Getters and Setters
}

