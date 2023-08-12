package server.nanum.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookExceptionHandler {
    private final RestTemplate restTemplate;

    @Value("${discord.webhookUrl:}") // Load value from application.yml
    private String discordWebhookUrl;

    public void sendExceptionWithDiscord(Exception ex) {
        if (discordWebhookUrl == null) {
            return; // Exit the method if the URL is not configured
        }

        int maxMessageLength = 1800;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();

            StringBuilder description = new StringBuilder();
            description.append("**").append(ex.getMessage()).append("**\n");

            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : ex.getStackTrace()) {
                String elementString = element.toString() + "\n";
                if (stackTrace.length() + elementString.length() <= maxMessageLength) {
                    stackTrace.append(elementString);
                } else {
                    break;
                }
            }

            description.append("```").append(stackTrace).append("...```");
            description.append("\n\n").append(Instant.now().toString()); // Adding the timestamp

            payload.put("embeds", new Object[]{
                    new HashMap<String, Object>() {{
                        put("title", "\uD83D\uDEA8 예외 발생!");
                        put("description", description.toString());
                        put("color", 16711680);
                    }}
            });

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    discordWebhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Exception notification sent to Discord successfully.");
            } else {
                log.error("Failed to send exception notification to Discord. Response code: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("Error sending exception notification to Discord: " + e.getMessage());
        }
    }
}
