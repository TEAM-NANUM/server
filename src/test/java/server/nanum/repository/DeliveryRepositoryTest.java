package server.nanum.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.domain.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
@Import(QuerydslConfig.class)
public class DeliveryRepositoryTest {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    UserRepository userRepository;
    User user1 = User.builder().userRole(UserRole.HOST).name("1번").uid(1L).build();
    User user2 = User.builder().userRole(UserRole.GUEST).name("2번").uid(2L).build();
    User user3 = User.builder().userRole(UserRole.HOST).name("3번").uid(3L).build();
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;
    Delivery delivery4;
    Delivery delivery5;
    @BeforeEach
    public void setUp(){

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        delivery1 = Delivery.builder().id(1L).nickname("1번").phoneNumber("1").isDefault(true).user(user1).build();
        delivery2 = Delivery.builder().id(2L).nickname("2번").phoneNumber("2").isDefault(true).user(user2).build();
        delivery3 = Delivery.builder().id(3L).nickname("3번").phoneNumber("3").isDefault(true).user(user3).build();
        delivery4 = Delivery.builder().id(4L).nickname("4번").phoneNumber("4").isDefault(false).user(user1).build();
        delivery5 = Delivery.builder().id(5L).nickname("5번").phoneNumber("5").isDefault(false).user(user1).build();

        deliveryRepository.save(delivery1);
        deliveryRepository.save(delivery2);
        deliveryRepository.save(delivery3);
        deliveryRepository.save(delivery4);
        deliveryRepository.save(delivery5);



    }
    @Test
    @Order(1)
    @DisplayName("기본 주소 찾기")
    public void defaultFind(){
        Delivery findDelivery1 = deliveryRepository.findByUserAndIsDefaultTrue(user1).get();
        Delivery findDelivery2 = deliveryRepository.findByUserAndIsDefaultTrue(user2).get();
        Delivery findDelivery3 = deliveryRepository.findByUserAndIsDefaultTrue(user3).get();
        assertAll(
                ()->assertEquals("1",findDelivery1.getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("2",findDelivery2.getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("3",findDelivery3.getPhoneNumber(),()->"다른 주소임")
        );

    }
    @Test
    @Order(2)
    @DisplayName("주소 찾기")
    public void deliveryFind(){
        delivery1 = Delivery.builder().id(1L).nickname("1번").phoneNumber("1").isDefault(true).user(user1).build();
        delivery2 = Delivery.builder().id(2L).nickname("2번").phoneNumber("2").isDefault(true).user(user2).build();
        delivery3 = Delivery.builder().id(3L).nickname("3번").phoneNumber("3").isDefault(true).user(user3).build();
        Delivery t1 = deliveryRepository.save(delivery1);
        Delivery t2 = deliveryRepository.save(delivery2);
        Delivery t3 = deliveryRepository.save(delivery3);

        Delivery findDelivery1 = deliveryRepository.findByIdAndUser(t1.getId(),user1).get();
        Delivery findDelivery2 = deliveryRepository.findByIdAndUser(t2.getId(),user2).get();
        Delivery findDelivery3 = deliveryRepository.findByIdAndUser(t3.getId(),user3).get();

        assertAll(
                ()->assertEquals("1",findDelivery1.getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("2",findDelivery2.getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("3",findDelivery3.getPhoneNumber(),()->"다른 주소임")
        );
    }
    @Test
    @Order(3)
    @DisplayName("유저로 전체 조회")
    public void allByUser(){
        List<Delivery> deliveryList = deliveryRepository.findByUser(user1);
        assertAll(
                ()->assertEquals(3,deliveryList.size(),()->"개수 다름"),
                ()->assertEquals("1",deliveryList.get(0).getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("4",deliveryList.get(1).getPhoneNumber(),()->"다른 주소임"),
                ()->assertEquals("5",deliveryList.get(2).getPhoneNumber(),()->"다른 주소임")
        );
    }
}
