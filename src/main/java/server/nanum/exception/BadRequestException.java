package server.nanum.exception;

/**
 * 404 예외 처리
 *
 * 작성자: Jinyeong Seol
 * 버전: 1.0.0
 * 작성일: 2023-08-05
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

