package server.nanum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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

    @Captor
    private ArgumentCaptor<Delivery> deliveryArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserDeliveries() {
        // Given
        User user = User.builder().build();
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByUser(user)).thenReturn(Collections.singletonList(delivery));

        // When
        DeliveryListResponse response = deliveryService.getUserDeliveries(user);

        // Then
        assertEquals(1, response.deliveryResponses().size());
        assertEquals(delivery.getId(), response.deliveryResponses().get(0).deliveryId());
    }

    @Test
    public void testSave() {
        // Given
        User user = User.builder().build();
        DeliveryRequestDTO request = createDummyDeliveryRequest();
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(Delivery.builder().build());

        // When
        assertDoesNotThrow(() -> deliveryService.save(request, user));

        // Then
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    public void testToggleDefaultStatus() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.toggleDefaultStatus(id, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
    }

    @Test
    public void testToggleDefaultStatusWithInvalidId() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> deliveryService.toggleDefaultStatus(id, user));
    }

    @Test
    public void testUpdate() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        DeliveryRequestDTO request = createDummyDeliveryRequest();
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.update(id, request, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
        // 추가로 필요한 확인사항이 있다면 여기에 추가
    }

    @Test
    public void testDelete() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(delivery));

        // When
        assertDoesNotThrow(() -> deliveryService.delete(id, user));

        // Then
        verify(deliveryRepository, times(1)).findByIdAndUser(id, user);
        verify(deliveryRepository, times(1)).delete(delivery);
    }

    @Test
    public void testDeleteWithInvalidId() {
        // Given
        User user = User.builder().build();
        Long id = 1L;
        when(deliveryRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> deliveryService.delete(id, user));
    }

    @Test
    public void testHandleFirstDelivery() {
        // Given
        User user = User.builder().build();
        when(deliveryRepository.countByUser(user)).thenReturn(0);

        // When
        deliveryService.save(createDummyDeliveryRequest(), user);

        // Then
        verify(deliveryRepository, times(1)).save(deliveryArgumentCaptor.capture());
        assertTrue(deliveryArgumentCaptor.getValue().isDefault());
    }

    @Test
    public void testUpdateDefaultStatusMakeDefault() {
        // Given
        User user = User.builder().build();
        Delivery delivery = createDummyDelivery();
        delivery.resetDefault();
        when(deliveryRepository.findByIdAndUser(anyLong(), eq(user))).thenReturn(Optional.of(delivery));

        // When
        deliveryService.toggleDefaultStatus(1L, user);

        // Then
        assertTrue(delivery.isDefault());
    }

    @Test
    public void testUpdateDefaultStatusResetDefault() {
        // Given
        User user = User.builder().build();
        Delivery delivery = createDummyDelivery();
        when(deliveryRepository.findByIdAndUser(anyLong(), eq(user))).thenReturn(Optional.of(delivery));

        // When
        deliveryService.toggleDefaultStatus(1L, user);

        // Then
        assertFalse(delivery.isDefault());
    }

    @Test
    public void testHandleDefaultDeletion() {
        // Given
        User user = User.builder().build();

        Delivery defaultDelivery = createDummyDelivery();
        Delivery anotherDelivery = mock(Delivery.class); // Mock 객체 생성

        when(deliveryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(defaultDelivery));
        when(deliveryRepository.findByUser(user)).thenReturn(Arrays.asList(anotherDelivery, defaultDelivery)); // 순서 변경

        // When
        deliveryService.delete(1L, user);

        // Then
        verify(deliveryRepository, times(1)).delete(defaultDelivery);

        // 새로운 기본 배송지가 anotherDelivery가 되어야 함을 확인
        verify(anotherDelivery, times(1)).makeDefault(); // 이제 anotherDelivery는 Mock 객체이므로 검증 가능
    }

    @Test
    public void testResetDefault() {
        // Given
        User user = User.builder().build();
        Delivery delivery = createDummyDelivery();
        delivery.makeDefault();

        // mock 처리 추가
        when(deliveryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.findByUserAndIsDefaultTrue(user)).thenReturn(Optional.of(delivery));

        // When
        deliveryService.toggleDefaultStatus(1L, user);

        // Then
        assertFalse(delivery.isDefault());
    }

    private Delivery createDummyDelivery() {
        return createDummyDelivery(true);
    }

    private Delivery createDummyDelivery(boolean isDefault) {
        AddressDTO addressDTO = createDummyAddressDTO();

        return Delivery.builder()
                .id(1L)
                .receiver("Receiver")
                .nickname("Nickname")
                .phoneNumber("123-456-7890")
                .address(addressDTO.toAddress())
                .isDefault(isDefault)
                .build();
    }

    private AddressDTO createDummyAddressDTO() {
        return AddressDTO.builder()
                .zipCode("123-456")
                .defaultAddress("Default Address")
                .detailAddress("Detail Address")
                .build();
    }

    private DeliveryRequestDTO createDummyDeliveryRequest() {
        AddressDTO addressDTO = createDummyAddressDTO();

        return DeliveryRequestDTO.builder()
                .receiver("Receiver")
                .nickname("Nickname")
                .phoneNumber("123-456-7890")
                .addressDTO(addressDTO)
                .build();
    }
}