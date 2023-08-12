package server.nanum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.dto.user.response.UserGroupListResponseDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.UserGroupRepository;

import static server.nanum.dto.user.response.UserGroupListResponseFactory.createResponse;

/**
 * 그룹 서비스
 * 사용자 그룹과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *  @author hyunjin
 *   @version 1.0.0
 *   @since 2023-08-05
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final DeliveryRepository deliveryRepository;
    private final UserGroupRepository userGroupRepository;

    /**
     * 사용자 그룹에 속한 정보들을 조회하여 UserGroupListResponseDTO 형태로 반환합니다.
     *
     * @param user 현재 사용자 정보
     * @return UserGroupListResponseDTO 사용자 그룹 정보 목록
     */
    public UserGroupListResponseDTO getGroupList(User user) {
        UserGroup userGroup = user.getUserGroup();

        return createResponse(
                userGroup,
                user.getName(),
                userGroupRepository.findUsersByUserGroupId(userGroup.getId()),
                this::getDefaultAddress);
    }

    /**
     * 주어진 사용자의 기본 배송지를 조회하여 반환합니다.
     * 기본 배송지가 없는 경우 NotFoundException을 발생시킵니다.
     *
     * @param guest 기본 배송지를 조회할 사용자
     * @return String 기본 배송지 주소
     * @throws NotFoundException 기본 배송지가 없는 경우 발생
     */
    private String getDefaultAddress(User guest) {
        return deliveryRepository.findByUserAndIsDefaultTrue(guest)
                .map(Delivery::getAddress)
                .map(Address::getDefaultAddress)
                .orElseThrow(() -> new NotFoundException("Guest의 배송지가 등록되어 있지 않습니다!"));
    }
}
