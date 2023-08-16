package server.nanum.service.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.nanum.domain.User;
import server.nanum.domain.UserRole;
import server.nanum.dto.user.request.GuestLoginRequestDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.LoginResponseDTO.UserResponseDTO;
import server.nanum.dto.user.response.LoginResponseFactory;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.UserRepository;
import server.nanum.utils.JwtProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestUserAdapterTest {

    @InjectMocks
    private GuestUserAdapter guestUserAdapter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private LoginResponseFactory loginResponseFactory;

    private static final String VALID_INVITE_CODE = "testInviteCode";
    private static final String INVALID_INVITE_CODE = "invalidInviteCode";

    @Test
    void supportsTest() {
        UserLoginRequestDTO guestLoginRequestDTO = GuestLoginRequestDTO.builder().build();
        UserLoginRequestDTO otherLoginRequestDTO = mock(UserLoginRequestDTO.class);

        assertTrue(guestUserAdapter.supports(guestLoginRequestDTO));
        assertFalse(guestUserAdapter.supports(otherLoginRequestDTO));
    }

    @Test
    void loginTest() {
        User user = createUser();
        GuestLoginRequestDTO guestLoginRequestDTO = createGuestLoginRequest(VALID_INVITE_CODE);
        String expectedToken = "expectedToken";
        LoginResponseDTO expectedResponse = createExpectedResponse(user, expectedToken);

        when(userRepository.findByInviteCode(eq(VALID_INVITE_CODE))).thenReturn(Optional.of(user));
        when(jwtProvider.createToken(eq(formatToken(user)))).thenReturn(expectedToken);
        when(loginResponseFactory.createLoginResponseDTO(eq(user), eq(expectedToken))).thenReturn(expectedResponse);

        LoginResponseDTO actualResponse = guestUserAdapter.login(guestLoginRequestDTO);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void loginWithInvalidInviteCodeTest() {
        GuestLoginRequestDTO guestLoginRequestDTO = createGuestLoginRequest(INVALID_INVITE_CODE);

        when(userRepository.findByInviteCode(eq(INVALID_INVITE_CODE))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> guestUserAdapter.login(guestLoginRequestDTO));
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("나눔이")
                .userRole(UserRole.GUEST)
                .inviteCode(GuestUserAdapterTest.VALID_INVITE_CODE)
                .build();
    }

    private GuestLoginRequestDTO createGuestLoginRequest(String inviteCode) {
        return GuestLoginRequestDTO.builder()
                .inviteCode(inviteCode)
                .build();
    }

    private String formatToken(User user) {
        return String.format("%s:%s", user.getId(), user.getUserRole());
    }

    private LoginResponseDTO createExpectedResponse(User user, String token) {
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId().toString())
                .username(user.getName())
                .role(user.getUserRole().name())
                .build();

        return LoginResponseDTO.builder()
                .token(token)
                .userResponseDTO(userResponseDTO)
                .build();
    }
}