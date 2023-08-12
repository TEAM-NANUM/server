package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.annotation.CurrentUser;
import server.nanum.domain.User;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.service.SignupService;

/**
 * 회원가입 컨트롤러
 * 게스트용 회원가입과 판매자용 회원가입 API를 제공합니다.
 *@author hyunjin
 * @version 1.0.0
 * @since 2023-08-05
 */
@Tag(name = "회원가입 관련 API", description = "회원가입  관련 API입니다. Guest 등록 및 판매자 회원 가입을 수행합니다.")
@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    /**
     * 게스트용 회원가입 API
     *
     * @param user 현재 사용자 정보
     * @param guestSignupDTO 게스트 회원가입 요청 DTO
     * @return ResponseEntity<Void> 회원가입 결과 응답
     */
    @Operation(summary = "Guest 등록 API", description = "Host가 Guest를 등록할 때 사용하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 성공!",  content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "토큰이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "토큰은 있으나 권한이 없는 경우", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasRole('ROLE_HOST')")
    @PostMapping("/guest")
    public ResponseEntity<Void> registerGuest(@CurrentUser User user, @Valid @RequestBody GuestSignupDTO guestSignupDTO) {

        signupService.registerGuest(user, guestSignupDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 판매자용 회원가입 API
     *
     * @param sellerSignupDTO 판매자 회원가입 요청 DTO
     * @return ResponseEntity<Void> 회원가입 결과 응답
     */
    @Operation(summary = "판매자 회원가입 API", description = "판매자가 회원가입을 할 때 사용하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 성공!",  content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/seller")
    public ResponseEntity<Void> registerSeller(@Valid @RequestBody SellerSignupDTO sellerSignupDTO) {

        signupService.registerSeller(sellerSignupDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
