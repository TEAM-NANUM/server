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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.error.DefaultErrorResponse;
import server.nanum.dto.error.ErrorDTO;
import server.nanum.dto.user.response.HostGetResponseDTO;
import server.nanum.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "사용자 관련 API", description = "사용자 관련 API입니다. 사용자 조회, 로그아웃, 사용자 탈퇴, 포인트 충전을 수행합니다.")
public class UserController {

    private final UserService userService;

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
}
