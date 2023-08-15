package server.nanum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import server.nanum.dto.user.request.GuestLoginRequestDTO;
import server.nanum.dto.user.request.HostLoginRequestDTO;
import server.nanum.dto.user.request.SellerLoginRequestDTO;
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

    @Test
    void guestUserLogin_Success() {
        // Arrange
        GuestLoginRequestDTO guestLoginRequest = GuestLoginRequestDTO.builder().build();
        UserAdapter guestUserAdapter = mock(GuestUserAdapter.class);
        LoginResponseDTO expectedResponse = LoginResponseDTO.builder().build();

        when(userAdapters.stream()).thenReturn(Stream.of(guestUserAdapter));
        when(guestUserAdapter.supports(guestLoginRequest)).thenReturn(true);
        when(guestUserAdapter.login(guestLoginRequest)).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO actualResponse = loginService.loginOrCreate(guestLoginRequest);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userAdapters, times(1)).stream();
        verify(guestUserAdapter, times(1)).supports(guestLoginRequest);
        verify(guestUserAdapter, times(1)).login(guestLoginRequest);
    }

    @Test
    void hostUserLogin_Success() {
        // Arrange
        HostLoginRequestDTO hostLoginRequest = HostLoginRequestDTO.builder().build();
        UserAdapter hostUserAdapter = mock(HostUserAdapter.class);
        LoginResponseDTO expectedResponse = LoginResponseDTO.builder().build();

        when(userAdapters.stream()).thenReturn(Stream.of(hostUserAdapter));
        when(hostUserAdapter.supports(hostLoginRequest)).thenReturn(true);
        when(hostUserAdapter.login(hostLoginRequest)).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO actualResponse = loginService.loginOrCreate(hostLoginRequest);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userAdapters, times(1)).stream();
        verify(hostUserAdapter, times(1)).supports(hostLoginRequest);
        verify(hostUserAdapter, times(1)).login(hostLoginRequest);
    }

    @Test
    void sellerUserLogin_Success() {
        // Arrange
        SellerLoginRequestDTO sellerLoginRequest = SellerLoginRequestDTO.builder().build();
        UserAdapter sellerUserAdapter = mock(SellerUserAdapter.class);
        LoginResponseDTO expectedResponse = LoginResponseDTO.builder().build();

        when(userAdapters.stream()).thenReturn(Stream.of(sellerUserAdapter));
        when(sellerUserAdapter.supports(sellerLoginRequest)).thenReturn(true);
        when(sellerUserAdapter.login(sellerLoginRequest)).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO actualResponse = loginService.loginOrCreate(sellerLoginRequest);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userAdapters, times(1)).stream();
        verify(sellerUserAdapter, times(1)).supports(sellerLoginRequest);
        verify(sellerUserAdapter, times(1)).login(sellerLoginRequest);
    }
}
