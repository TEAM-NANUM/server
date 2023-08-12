package server.nanum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import server.nanum.dto.error.ErrorDTO;
import server.nanum.exception.*;
import server.nanum.utils.WebhookExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final WebhookExceptionHandler webhookExceptionHandler;

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

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictException(ConflictException ex){
        ErrorDTO errorDTO = new ErrorDTO("Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<ErrorDTO> handlePaymentRequired(PaymentRequiredException ex){
        ErrorDTO errorDTO = new ErrorDTO("Payment Required", ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(errorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> dtoValidation(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String combinedErrorMessage = String.join(", ", errors.values());

        ErrorDTO errorDTO = new ErrorDTO("올바르지 않은 입력값입니다!", combinedErrorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDTO);
    }

    // handling 되지 않은 모든 예외를 catch
    @ExceptionHandler(Exception.class)
    public void handleUnhandledException(Exception ex) throws Exception {
        webhookExceptionHandler.sendExceptionWithDiscord(ex);
        throw ex;
    }
}
