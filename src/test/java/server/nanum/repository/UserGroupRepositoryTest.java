package server.nanum.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.domain.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class UserGroupRepositoryTest {
    @Autowired
    UserGroupRepository userGroupRepository;
    @Autowired
    UserRepository userRepository;
    UserGroup userGroup1 = UserGroup.builder().point(10000000).build();
    UserGroup userGroup2 = UserGroup.builder().point(10000).build();
    @BeforeEach
    public void setUp(){
        userGroupRepository.save(userGroup1);
        userGroupRepository.save(userGroup2);
        User user1 = User.builder().userRole(UserRole.HOST).name("1번").userGroup(userGroup1).uid(1L).build();
        User user2 = User.builder().userRole(UserRole.GUEST).name("2번").userGroup(userGroup2).uid(2L).build();
        User user3 = User.builder().userRole(UserRole.HOST).name("3번").userGroup(userGroup1).uid(3L).build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
    @Test
    @Order(1)
    @DisplayName("유저그룹 id로 유저조회")
    public void findByUserGroupId(){
        List<User> userList = userGroupRepository.findUsersByUserGroupId(userGroup1.getId());
        assertAll(
                ()->assertEquals(2,userList.size(),()->"개수 틀림"),
                ()->assertEquals(1L,userList.get(0).getUid(),()->"유저 틀림"),
                ()->assertEquals(3L,userList.get(1).getUid(),()->"유저 틀림")

        );
    }


}
