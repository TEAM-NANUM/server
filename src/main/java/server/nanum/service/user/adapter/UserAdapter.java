package server.nanum.service.user.adapter;

import server.nanum.domain.dto.user.AuthResponseDTO;
import server.nanum.domain.dto.user.UserDTO;

/**
 * 사용자 어댑터 인터페이스
 * 다양한 사용자 타입에 맞게 로그인 또는 회원 가입을 처리하기 위한 인터페이스입니다.
 * 구체적인 사용자 어댑터 클래스들은 이 인터페이스를 구현하여 사용자 타입에 따른 로그인 또는 회원 가입을 구현해야 합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
public interface UserAdapter {
    /**
     * 해당 사용자 어댑터가 주어진 사용자 정보를 지원하는지 여부를 판별합니다.
     *
     * @param userDTO 사용자 정보 객체
     * @return 해당 사용자 어댑터가 주어진 사용자 정보를 지원하는 경우 true, 그렇지 않은 경우 false
     */
    boolean supports(UserDTO userDTO);

    /**
     * 주어진 사용자 정보를 기반으로 로그인 또는 회원 가입을 처리합니다.
     *
     * @param userDTO 로그인 또는 회원 가입에 필요한 사용자 정보 객체
     * @return 인증 응답 DTO
     */
    AuthResponseDTO loginOrRegister(UserDTO userDTO);
}

