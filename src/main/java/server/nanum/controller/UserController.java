package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.error.DefaultErrorResponse;
import server.nanum.dto.error.ErrorDTO;
import server.nanum.dto.user.request.ChargeRequestDTO;
import server.nanum.dto.user.response.HostGetResponseDTO;
import server.nanum.service.UserService;

/**
 * 사용자 관련 API
 * Host 및 Guest가 사용자 조회, 회원 탈퇴, 포인트 충전을 하는 API입니다.
 *
 * @version 1.0.0
 * @since 2023-08-05
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "사용자 관련 API", description = "사용자 관련 API입니다. 사용자 조회, 로그아웃, 사용자 탈퇴, 포인트 충전을 수행합니다.")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 정보 조회 API
     *
     * @param user 현재 사용자
     * @return HostGetResponseDTO 사용자 정보 응답 DTO
     */
    @Operation(summary = "사용자 조회 API", description = "GUEST, HOST의 마이페이지에서 사용자정보를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!",  content = @Content(mediaType = "application/json" ,schema = @Schema(implementation =HostGetResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @GetMapping
    public HostGetResponseDTO getUserInfo(@CurrentUser User user) {
        return userService.getUserInfo(user);
    }

    /**
     * 포인트 충전 API
     *
     * @param user 현재 사용자
     * @param chargeRequestDTO 포인트 충전 요청 DTO
     * @return ResponseEntity<Void> 충전 성공 응답
     */
    @Operation(summary = "포인트 충전 API", description = "HOST가 포인트를 충전하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "충전 성공!",  content = @Content(schema =@Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasRole('ROLE_HOST')")
    @PutMapping("/charge")
    public ResponseEntity<Void> chargeUserPoint(@CurrentUser User user, @RequestBody ChargeRequestDTO chargeRequestDTO) {
        userService.chargePoint(user, chargeRequestDTO.point());
        return ResponseEntity.noContent().build();
    }

    /**
     * 사용자 탈퇴 API
     *
     * @param user 현재 사용자
     * @return ResponseEntity<Void> 탈퇴 성공 응답
     */
    @Operation(summary = "회원 탈퇴 API", description = "사용자가 탈퇴하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "탈퇴 성공!",  content = @Content(schema =@Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_GUEST')")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@CurrentUser User user) {
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }
}

