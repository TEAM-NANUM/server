package server.nanum.domain;

import jakarta.persistence.*;
import lombok.*;


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
    public void setPoint(int point){
        this.point=point;
    // UserGroup 생성하는 메서드
    public static UserGroup createUserGroup(int point) {
        return UserGroup.builder()
                .point(point)
                .build();
    }
}
