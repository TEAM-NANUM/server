package server.nanum.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import server.nanum.dto.error.ErrorDTO;

import java.io.IOException;

public class JWTErrorResponseWriter {

    /**
     * HTTP 응답에 에러 정보를 JSON 형태로 작성합니다.
     *
     * @param response HTTP 응답 객체
     * @param message 에러 메시지
     * @throws IOException 입출력 예외
     */

    public static void write(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorDTO errorDto = new ErrorDTO("JWT 에러 발생!!", message);
        String json = ObjectMapperUtil.getInstance().writeValueAsString(errorDto);

        response.getWriter().write(json);
    }

}
