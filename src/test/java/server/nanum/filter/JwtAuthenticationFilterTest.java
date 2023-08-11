package server.nanum.filter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMissingAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("JWT 에러 발생!!"))
                .andExpect(jsonPath("$.message").value("현재 요청에 인증헤더가 포함되어있지 않습니다!"));
    }
}

