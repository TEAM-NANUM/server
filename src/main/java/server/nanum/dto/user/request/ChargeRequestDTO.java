package server.nanum.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChargeRequestDTO(
        @NotNull(message = "충전할 포인트를 입력해주세요")
        @Positive(message = "충전할 포인트는 0보다 커야 합니다!")
        @Schema(example = "10000",description = "충전할 포인트")
        Integer point) { }
