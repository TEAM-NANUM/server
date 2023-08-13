package server.nanum.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import server.nanum.config.QuerydslConfig;
import server.nanum.domain.Address;
import server.nanum.domain.Seller;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@Import(QuerydslConfig.class)
public class SellerRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    public void testSaveAndFindByEmail() {
        Seller seller = Seller.builder()
                .name("Test Seller")
                .phoneNumber("123-456-7890")
                .email("test@example.com")
                .password("testPassword")
                .address(new Address("123 Street", "City", "12345"))
                .build();

        seller.withPoint(0L);
        seller.withEncryptedPassword("encryptedPassword");

        sellerRepository.save(seller);

        Seller foundSeller = sellerRepository.findByEmail("test@example.com").orElse(null);

        assertNotNull(foundSeller);
        assertEquals("Test Seller", foundSeller.getName());
        assertEquals("123-456-7890", foundSeller.getPhoneNumber());
        assertEquals("encryptedPassword", foundSeller.getPassword());
        assertNotNull(foundSeller.getAddress());
        assertEquals(0L, foundSeller.getPoint());
    }

    @Test
    public void testExistsByEmail() {
        Seller seller = Seller.builder()
                .name("Test Seller")
                .phoneNumber("123-456-7890")
                .email("test@example.com")
                .password("testPassword")
                .address(new Address("123 Street", "City", "12345"))
                .build();

        sellerRepository.save(seller);

        assertTrue(sellerRepository.existsByEmail("test@example.com"));
        assertFalse(sellerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    public void testExistsByPhoneNumber() {
        Seller seller = Seller.builder()
                .name("Test Seller")
                .phoneNumber("123-456-7890")
                .email("test@example.com")
                .password("testPassword")
                .address(new Address("123 Street", "City", "12345"))
                .build();

        sellerRepository.save(seller);

        assertTrue(sellerRepository.existsByPhoneNumber("123-456-7890"));
        assertFalse(sellerRepository.existsByPhoneNumber("987-654-3210"));
    }
}


