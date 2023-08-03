package server.nanum.dto;

import java.time.LocalDateTime;

public record ErrorDTO(String error, String message, LocalDateTime timestamp) {
    public ErrorDTO(String error, String message) {
        this(error, message, LocalDateTime.now());
    }
}