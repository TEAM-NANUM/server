package server.nanum.exception;

/**
 * 404 예외 처리
 *
 * 작성자: Jinyeong Seol
 * 버전: 1.0.0
 * 작성일: 2023-08-05
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

