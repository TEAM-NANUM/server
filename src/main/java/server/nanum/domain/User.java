package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import server.nanum.dto.user.request.HostLoginRequestDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "uid", unique = true)
    private Long uid;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "invite_code")
    private String inviteCode;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    // Host 사용자를 생성하는 메서드
    public static User createHost(HostLoginRequestDTO hostDTO, UserGroup userGroup) {
        return User.builder()
                .uid(hostDTO.uid())
                .name(hostDTO.name())
                .userRole(UserRole.HOST)
                .userGroup(userGroup)
                .build();
    }


    // 사용자가 속한 UserGroup의 포인트를 반환하는 메서드
    public int getUserGroupPoint() {
        return userGroup.getPoint();
    }
}
