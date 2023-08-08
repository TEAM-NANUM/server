package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@Table(name = "user_group")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @Column(name = "point")
    private int point;

    @OneToMany(mappedBy = "userGroup")
    private List<User> users;

    public void setPoint(int point) {
        this.point = point;
    }
    // UserGroup 생성하는 메서드
    public static UserGroup createUserGroup(int point) {
        return UserGroup.builder()
                .point(point)
                .build();
    }

    public List<User> getUsersList() {
        if (users == null) {
            throw new IllegalStateException("UserGroup의 사용자들이 존재하지 않습니다!");
        }
        return new ArrayList<>(users);
    }
}
