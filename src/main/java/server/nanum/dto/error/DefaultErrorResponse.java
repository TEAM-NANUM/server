package server.nanum.dto.error;

public record DefaultErrorResponse(String timestamp, int status, String error, String path) {
}

