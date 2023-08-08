package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import server.nanum.dto.response.UserGroupListResponseDTO;
import server.nanum.service.GroupService;

/**
 * 그룹 조회 API
 * Host가 자신이 등록한 Guest를 조회하는 API입니다.
 *  @author hyunjin
 *  @version 1.0.0
*   @since 2023-08-05
 */
@Tag(name = "그룹 조회 API", description = "Host가 자신이 등록한 Guest를 조회하는 API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    /**
     * 그룹 조회 API
     *
     * @param user 현재 사용자 정보
     * @return UserGroupListResponseDTO 그룹 정보 목록
     */
    @Operation(summary = "그룹 조회 API", description = "Host가 자신이 등록한 Guest를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공!", content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = UserGroupListResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Guest의 정보가 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_HOST')")
    public UserGroupListResponseDTO getDeliveryList(@CurrentUser User user) {
        return groupService.getGroupList(user);
    }
}
