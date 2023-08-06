package server.nanum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.nanum.dto.error.ErrorDTO;
import server.nanum.exception.BadRequestException;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.exception.KakaoClientException;
import server.nanum.exception.NotFoundException;

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        ErrorDTO errorDto = new ErrorDTO("Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException ex) {
        ErrorDTO errorDto = new ErrorDTO("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
