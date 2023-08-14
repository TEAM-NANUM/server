package server.nanum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.delivery.DeliveryListResponse;
import server.nanum.dto.delivery.DeliveryRequestDTO;
import server.nanum.dto.request.AddressDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.DeliveryRepository;

public class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDeliveryList() {
        // Given
        User user = User.builder().build();
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByUser(user)).thenReturn(Collections.singletonList(delivery));

        // When
        DeliveryListResponse response = deliveryService.getDeliveryList(user);

        // Then
        assertEquals(1, response.deliveryResponses().size());
        assertEquals(delivery.getId(), response.deliveryResponses().get(0).deliveryId());
    }

    @Test
    public void testSaveDelivery() {
        // Given
        User user = User.builder().build();
        DeliveryRequestDTO request = createDummyDeliveryRequest();
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(Delivery.builder().build());

        // When
        assertDoesNotThrow(() -> deliveryService.saveDelivery(request, user));

        // Then
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    public void testToggleDefault() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.toggleDefault(id, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
    }

    @Test
    public void testToggleDefaultWithInvalidId() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> deliveryService.toggleDefault(id, user));
    }

    @Test
    public void testUpdateDelivery() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        DeliveryRequestDTO request = createDummyDeliveryRequest();
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.updateDelivery(id, request, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
        assertEquals(request.getReceiver(), delivery.getReceiver());
        assertEquals(request.getNickname(), delivery.getNickname());
        assertEquals(request.getPhoneNumber(), delivery.getPhoneNumber());

        System.out.println(request.getAddressDTO().toAddress() + ", " + delivery.getAddress());
        assertEquals(request.getAddressDTO().toAddress(), delivery.getAddress());
    }

    @Test
    public void testDeleteDelivery() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.deleteDelivery(id, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
        verify(deliveryRepository, times(1)).delete(delivery);
    }

    @Test
    public void testDeleteDeliveryWithInvalidId() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> deliveryService.deleteDelivery(id, user));
    }

    private Delivery createDummyDelivery() {
        AddressDTO addressDTO = AddressDTO.builder()
                .zipCode("123-456")
                .defaultAddress("Default Address")
                .detailAddress("Detail Address")
                .build();

        return Delivery.builder()
                .id(1L)
                .address(addressDTO.toAddress())
                .build();
    }


    private DeliveryRequestDTO createDummyDeliveryRequest() {
        AddressDTO addressDTO = AddressDTO.builder()
                .zipCode("123-456")
                .defaultAddress("Default Address")
                .detailAddress("Detail Address")
                .build();

        return DeliveryRequestDTO.builder()
                .receiver("Receiver")
                .nickname("Nickname")
                .phoneNumber("123-456-7890")
                .addressDTO(addressDTO)
                .build();
    }


}