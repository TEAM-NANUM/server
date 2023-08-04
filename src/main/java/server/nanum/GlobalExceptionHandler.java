package server.nanum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.nanum.dto.ErrorDTO;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.exception.KakaoClientException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(KakaoClientException.class)
    public ResponseEntity<ErrorDTO> handleKakaoClientException(KakaoClientException ex) {
        ErrorDTO errorDto = new ErrorDTO("카카오 로그인 중 에러 발생!", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        ErrorDTO errorDto = new ErrorDTO("JWT 에러 발생!!", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }
}