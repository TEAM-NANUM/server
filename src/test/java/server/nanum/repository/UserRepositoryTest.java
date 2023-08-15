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
public class UserRepositoryTest {
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
        User user2 = User.builder().userRole(UserRole.GUEST).name("2번").inviteCode("2번").userGroup(userGroup1).uid(2L).build();
        User user3 = User.builder().userRole(UserRole.HOST).name("3번").userGroup(userGroup2).uid(3L).build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
    @Test
    @Order(1)
    @DisplayName("유저 uid로 유저조회")
    public void findByUid(){
        User user1 = userRepository.findByUid(1L).get();
        User user2 = userRepository.findByUid(3L).get();
        assertAll(
                ()->assertEquals("1번",user1.getName(),()->"유저 틀림"),
                ()->assertEquals("3번",user2.getName(),()->"유저 틀림")

        );
    }
    @Test
    @Order(2)
    @DisplayName("유저 id로 유저조회")
    public void findById(){
        User user1 = User.builder().userRole(UserRole.HOST).name("1번").userGroup(userGroup1).uid(5L).build();
        User user2 = User.builder().userRole(UserRole.GUEST).name("2번").inviteCode("2번").userGroup(userGroup2).uid(6L).build();
        User user3 = User.builder().userRole(UserRole.HOST).name("3번").userGroup(userGroup1).uid(7L).build();
        User u1 = userRepository.save(user1);
        User u2 = userRepository.save(user2);
        User u3 = userRepository.save(user3);
        User findUser1 = userRepository.findById(u1.getId()).get();
        User findUser2 = userRepository.findById(u2.getId()).get();
        assertAll(
                ()->assertEquals("1번",findUser1.getName(),()->"유저 틀림"),
                ()->assertEquals(10000000,findUser1.getUserGroup().getPoint(),()->"그룹 틀림"),
                ()->assertEquals("2번",findUser2.getName(),()->"유저 틀림"),
                ()->assertEquals(10000,findUser2.getUserGroup().getPoint(),()->"그룹 틀림")

        );
    }
    @Test
    @Order(3)
    @DisplayName("유저 초대코드로 유저조회")
    public void findByCode(){
        User user = userRepository.findByInviteCode("2번").get();
        assertAll(
                ()->assertEquals("2번",user.getName(),()->"유저 틀림")
        );
    }
    @Test
    @Order(4)
    @DisplayName("유저 그룹, 역할로 유저조회")
    public void findHost(){
        User hostUser = userRepository.findByUserGroupAndUserRole(userGroup1,UserRole.HOST).get();
        assertAll(
                ()->assertEquals("1번",hostUser.getName(),()->"유저 틀림")
        );
    }
    
}
