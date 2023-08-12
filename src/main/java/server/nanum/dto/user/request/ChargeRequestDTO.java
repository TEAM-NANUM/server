package server.nanum.dto.user.request;

import jakarta.validation.constraints.Positive;

public record ChargeRequestDTO(@Positive(message = "충전할 포인트는 0보다 커야 합니다!") int point) {
}
