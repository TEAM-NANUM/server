package server.nanum.exception;

/**
 * 카카오 클라이언트에서 발생하는 예외를 나타내는 클래스
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
public class KakaoClientException extends RuntimeException {

    public KakaoClientException(String message) {
        super(message);
    }

    public KakaoClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

