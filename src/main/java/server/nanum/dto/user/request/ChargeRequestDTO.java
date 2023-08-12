package server.nanum.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record ChargeRequestDTO(
        @Positive(message = "충전할 포인트는 0보다 커야 합니다!")
        @Schema(example = "10000")
        int point) { }
