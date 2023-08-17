package server.nanum.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import server.nanum.domain.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Service
public class DiscordWebHookService {
    private final RestTemplate restTemplate;
    @Value("${discord.loginWebhookUrl:}") // Load value from application.yml
    private String discordWebhookUrl;
//    private String discordWebhookUrl="https://discord.com/api/webhooks/1141633570721497098/iLjTstwS1imRqEIUVaQsk03qT63n9WNxbjvhbkT5oUbJ9ZiKlQ5CTAkiFCSppXPxx3Fa";
    public void sendLoginMessage(String userPk, String username, String userRole) {
        if(discordWebhookUrl.isEmpty()){
            return;
        }

        String message = userRole+" 번호: " +userPk + "  " + username + "님이 로그인 하셨습니다.";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> payload = new HashMap<>();

        payload.put("content", message);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
        restTemplate.exchange(discordWebhookUrl, HttpMethod.POST,entity,String.class);
//        log.info("response: {}",response);
        log.info(message);
    }
}





