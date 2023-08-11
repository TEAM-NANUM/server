package server.nanum.dto.user.response;

import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.dto.response.UserGroupListResponseDTO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static server.nanum.dto.response.UserGroupListResponseDTO.GuestDTO;
import static server.nanum.dto.response.UserGroupListResponseDTO.HostDTO;

/**
 * 사용자 그룹 정보를 생성하는 팩토리 클래스
 * UserGroupListResponseDTO를 생성하는 정적 메서드들을 제공합니다.
 *  @author hyunjin
 *  @version 1.0.0
 *  @since 2023-08-05
 */
public class UserGroupListResponseFactory {

    /**
     * UserGroup, 사용자 정보, 기본 배송지를 활용하여 UserGroupListResponseDTO를 생성합니다.
     *
     * @param userGroup 사용자 그룹 정보
     * @param userName  호스트의 이름
     * @param users     사용자 목록
     * @param defaultAddressProvider 기본 배송지를 제공하는 함수
     * @return UserGroupListResponseDTO 생성된 응답 DTO
     */
    public static UserGroupListResponseDTO createResponse(UserGroup userGroup,
                                                            String userName, List<User> users, Function<User, String> defaultAddressProvider) {

        HostDTO host = createHost(userGroup, userName);
        List<GuestDTO> guests = createGuests(users, defaultAddressProvider);

        return new UserGroupListResponseDTO(host, guests);
    }

    private static HostDTO createHost(UserGroup userGroup, String userName) {
        return new HostDTO(userName, userGroup.getPoint());
    }

    private static List<GuestDTO> createGuests(List<User> users, Function<User, String> defaultAddressProvider) {
        return users.stream()
                .filter(User::isGuest)
                .map(user -> createGuest(user, defaultAddressProvider.apply(user)))
                .collect(Collectors.toList());
    }

    private static GuestDTO createGuest(User guest, String defaultAddress) {
        return new GuestDTO(guest.getName(), defaultAddress, guest.getInviteCode());
    }
}