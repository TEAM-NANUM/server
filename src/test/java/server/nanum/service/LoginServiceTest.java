package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.nanum.dto.user.request.GuestLoginRequestDTO;
import server.nanum.dto.user.request.HostLoginRequestDTO;
import server.nanum.dto.user.request.SellerLoginRequestDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.service.adapter.GuestUserAdapter;
import server.nanum.service.adapter.HostUserAdapter;
import server.nanum.service.adapter.SellerUserAdapter;
import server.nanum.service.adapter.UserAdapter;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private List<UserAdapter> userAdapters;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideLoginRequests")
    void userLogin_Success(UserLoginRequestDTO userLoginRequest, UserAdapter userAdapter) {
        // Arrange
        LoginResponseDTO expectedResponse = LoginResponseDTO.builder().build();

        when(userAdapters.stream()).thenReturn(Stream.of(userAdapter));
        when(userAdapter.supports(userLoginRequest)).thenReturn(true);
        when(userAdapter.login(userLoginRequest)).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO actualResponse = loginService.loginOrCreate(userLoginRequest);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userAdapters, times(1)).stream();
        verify(userAdapter, times(1)).supports(userLoginRequest);
        verify(userAdapter, times(1)).login(userLoginRequest);
    }

    private static Stream<Arguments> provideLoginRequests() {
        return Stream.of(
                Arguments.of(GuestLoginRequestDTO.builder().build(), mock(GuestUserAdapter.class)),
                Arguments.of(HostLoginRequestDTO.builder().build(), mock(HostUserAdapter.class)),
                Arguments.of(SellerLoginRequestDTO.builder().build(), mock(SellerUserAdapter.class))
        );
    }
}
