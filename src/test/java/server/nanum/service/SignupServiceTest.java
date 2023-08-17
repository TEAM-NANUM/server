package server.nanum.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.nanum.domain.Delivery;
import server.nanum.domain.Seller;
import server.nanum.domain.User;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.exception.ConflictException;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.nanum.repository.UserRepository;
import server.nanum.service.DiscordWebHook.DiscordWebHookService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignupServiceTest {

    private SignupService signupService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;
    @Mock
    private DiscordWebHookService discordWebHookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signupService = new SignupService(deliveryRepository, sellerRepository, passwordEncoder, entityManager,discordWebHookService);
    }

    /**
     * 게스트 회원가입 시, 게스트 정보와 배송 정보가 올바르게 생성되고 저장되는지 확인하는 테스트 케이스.
     */
    @Test
    public void guestRegistration() {
        // Given
        User user = User.builder().build();
        GuestSignupDTO guestSignupDTO = createGuestSignupDTO();

        // When
        signupService.registerGuest(user, guestSignupDTO);
        // Then
        verify(entityManager, times(1)).persist(any(User.class));
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    /**
     * 판매자 회원가입 시, 중복된 이메일이나 전화번호가 없을 때 정상적으로 등록되는지 확인하는 테스트 케이스.
     */
    @Test
    public void SellerRegistration() {
        // Given
        SellerSignupDTO sellerSignupDTO = createNonDuplicateSellerSignupDTO();
        when(sellerRepository.existsByEmail(anyString())).thenReturn(false);
        when(sellerRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        // When
        signupService.registerSeller(sellerSignupDTO);

        // Then
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    /**
     * 중복된 이메일로 판매자 회원가입 시, ConflictException이 발생하는지 확인하는 테스트 케이스.
     */
    @Test
    public void duplicateEmailSellerRegistration() {
        // Given
        SellerSignupDTO sellerSignupDTO = createDuplicateEmailSellerSignupDTO();
        when(sellerRepository.existsByEmail(anyString())).thenReturn(true);

        // Then
        assertThrows(ConflictException.class, () -> signupService.registerSeller(sellerSignupDTO));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    /**
     * 중복된 전화번호로 판매자 회원가입 시, ConflictException이 발생하는지 확인하는 테스트 케이스.
     */
    @Test
    public void duplicatePhoneNumberSellerRegistration() {
        // Given
        SellerSignupDTO sellerSignupDTO = createDuplicatePhoneNumberSellerSignupDTO();
        when(sellerRepository.existsByEmail(anyString())).thenReturn(false);
        when(sellerRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        // Then
        assertThrows(ConflictException.class, () -> signupService.registerSeller(sellerSignupDTO));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    private GuestSignupDTO createGuestSignupDTO() {
        return GuestSignupDTO.builder()
                .nickname("Guest Name")
                .address(createAddressDTO())
                .build();
    }

    private SellerSignupDTO createNonDuplicateSellerSignupDTO() {
        return SellerSignupDTO.builder()
                .email("seller@example.com")
                .password("password123")
                .username("Seller Name")
                .phoneNumber("123-456-7890")
                .address(createAddressDTO())
                .build();
    }

    private SellerSignupDTO createDuplicateEmailSellerSignupDTO() {
        return SellerSignupDTO.builder()
                .email("duplicate@example.com")
                .build();
    }

    private SellerSignupDTO createDuplicatePhoneNumberSellerSignupDTO() {
        return SellerSignupDTO.builder()
                .phoneNumber("123-456-7890")
                .build();
    }

    private AddressDTO createAddressDTO() {
        return AddressDTO.builder()
                .zipCode("123-456")
                .defaultAddress("서울시 강남구")
                .detailAddress("삼성동 123번지")
                .build();
    }
}

