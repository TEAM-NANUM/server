package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import server.nanum.dto.user.HostDTO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User{
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    // Host 사용자를 생성하는 메서드
    public static User createHost(HostDTO hostDTO) {
        // 새로운 UserGroup 생성 및 초기화
        UserGroup newUserGroup = UserGroup.builder()
                .point(0) // point는 0으로 초기화
                .build();

        return User.builder()
                .uid(hostDTO.uid())
                .name(hostDTO.name())
                .userRole(UserRole.HOST)
                .userGroup(newUserGroup)
                .build();
    }

    // 사용자가 속한 UserGroup의 포인트를 반환하는 메서드
    public int getPoint() {
        return this.userGroup.getPoint();
    }

}
