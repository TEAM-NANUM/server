package server.nanum.service.DiscordWebHook;

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
    public void sendLoginMessage(UserStatus userStatus,String userPk, String username, String userRole) {
        if(discordWebhookUrl.isEmpty()){
            return;
        }
        String colorCode = ":white_check_mark: 로그인";
        String userStatusChange="5671FF";
        if(userStatus==UserStatus.LOGIN){
            userStatusChange=":white_check_mark:  로그인";
            colorCode="5671FF";
        } else if(userStatus==UserStatus.REGISTER){
            userStatusChange=":sparkles: 회원가입";
            colorCode="FFE500";
        } else if(userStatus==UserStatus.LOGOUT){
            userStatusChange="\uD83D\uDD12 로그아웃";
            colorCode="EE82EE";
        } else if(userStatus==UserStatus.WITHDRAWAL){
            userStatusChange="\uD83D\uDC94 회원탈퇴";
            colorCode="A9A9A9";
        }

        String userRoleChange="";
        if (userRole == "GUEST") {
            userRoleChange="게스트";
        }else if(userRole=="SELLER"){
            userRoleChange="판매자";
        }else if(userRole == "HOST"){
            userRoleChange="호스트";
        }

        String message = "\n역할: "+userRoleChange+"\nId: " +userPk + "\n닉네임: "+ username;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> payload = new HashMap<>();
            String finalUserStatusChange = userStatusChange;
            int intColorInt = Integer.parseInt(colorCode, 16);
            payload.put("embeds", new Object[]{
                    new HashMap<String, Object>() {{
                        put("title", finalUserStatusChange);
                        put("description", message);
                        put("color", intColorInt);
                    }}
            });

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    discordWebhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        }catch(Exception ex){
            log.error("Error sending login data to Discord: " + ex.getMessage());
        }
    }
}





