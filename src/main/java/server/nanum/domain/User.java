package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import server.nanum.domain.dto.user.KakaoUserDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    public static User createHost(KakaoUserDTO userInfo) {
        // 새로운 UserGroup 생성 및 초기화
        UserGroup newUserGroup = UserGroup.builder()
                .point(0L) // point는 0으로 초기화
                .build();

        return User.builder()
                .uid(userInfo.getUid())
                .name(userInfo.getName())
                .userRole(UserRole.HOST)
                .userGroup(newUserGroup)
                .build();
    }
}
