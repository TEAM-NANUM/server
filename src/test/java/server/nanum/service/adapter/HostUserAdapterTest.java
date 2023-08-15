package server.nanum.service.adapter;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.dto.user.request.HostLoginRequestDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.LoginResponseFactory;
import server.nanum.repository.UserRepository;
import server.nanum.utils.JwtProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HostUserAdapterTest {

    @InjectMocks
    private HostUserAdapter hostUserAdapter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private LoginResponseFactory loginResponseFactory;

    @Mock
    private EntityManager entityManager;

    private static final long UID = 11111111;
    private static final String EXPECTED_TOKEN = "expectedToken";

    private User user;
    private HostLoginRequestDTO hostLoginRequestDTO;
    private LoginResponseDTO expectedResponse;

    @BeforeEach
    void setUp() {
        hostLoginRequestDTO = HostLoginRequestDTO.builder().uid(UID).build();
        user = createUser(hostLoginRequestDTO);
        expectedResponse = createExpectedResponse();
    }

    @Test
    void SupportHostLoginRequest() {
        // Given
        UserLoginRequestDTO otherLoginRequestDTO = mock(UserLoginRequestDTO.class);

        // When
        boolean supportsHost = hostUserAdapter.supports(hostLoginRequestDTO);
        boolean supportsOther = hostUserAdapter.supports(otherLoginRequestDTO);

        // Then
        assertTrue(supportsHost);
        assertFalse(supportsOther);
    }

    @Test
    void LoginExistingUser() {
        // Given
        when(userRepository.findByUid(eq(UID))).thenReturn(Optional.of(user));
        when(jwtProvider.createToken(eq(formatToken(user)))).thenReturn(EXPECTED_TOKEN);
        when(loginResponseFactory.createLoginResponseDTO(eq(user), eq(EXPECTED_TOKEN))).thenReturn(expectedResponse);

        // When
        LoginResponseDTO actualResponse = hostUserAdapter.login(hostLoginRequestDTO);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void LoginNewUser() {
        // Given
        when(userRepository.findByUid(eq(UID))).thenReturn(Optional.empty());
        when(jwtProvider.createToken(eq(formatToken(user)))).thenReturn(EXPECTED_TOKEN);
        when(loginResponseFactory.createLoginResponseDTO(any(User.class), anyString())).thenReturn(expectedResponse);
        given(userRepository.save(any(User.class))).willReturn(user);

        // When
        LoginResponseDTO actualResponse = hostUserAdapter.login(hostLoginRequestDTO);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(entityManager, times(1)).persist(any(UserGroup.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    private User createUser(HostLoginRequestDTO hostLoginRequestDTO) {
        return User.createHost(hostLoginRequestDTO, UserGroup.createUserGroup(0));
    }

    private String formatToken(User user) {
        return String.format("%s:%s", user.getId(), user.getUserRole());
    }

    private LoginResponseDTO createExpectedResponse() {
        return LoginResponseDTO.builder()
                .token(HostUserAdapterTest.EXPECTED_TOKEN)
                .build();
    }
}
