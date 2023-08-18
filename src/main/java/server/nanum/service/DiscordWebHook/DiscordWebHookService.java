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
import server.nanum.domain.product.Product;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
//        private String discordWebhookUrl="https://discord.com/api/webhooks/1141633570721497098/iLjTstwS1imRqEIUVaQsk03qT63n9WNxbjvhbkT5oUbJ9ZiKlQ5CTAkiFCSppXPxx3Fa";

    public void sendLoginMessage(UserStatus userStatus, String userPk, String username, String userRole) {
        if (discordWebhookUrl.isEmpty()) {
            return;
        }
        String colorCode = ":white_check_mark: 로그인";
        String userStatusChange = "5671FF";
        if (userStatus == UserStatus.LOGIN) {
            userStatusChange = ":white_check_mark:  로그인";
            colorCode = "5671FF";
        } else if (userStatus == UserStatus.REGISTER) {
            userStatusChange = ":sparkles: 회원가입";
            colorCode = "FFE500";
        } else if (userStatus == UserStatus.LOGOUT) {
            userStatusChange = "\uD83D\uDD12 로그아웃";
            colorCode = "EE82EE";
        } else if (userStatus == UserStatus.WITHDRAWAL) {
            userStatusChange = "\uD83D\uDC94 회원탈퇴";
            colorCode = "A9A9A9";
        }

        String userRoleChange = "";
        if (userRole == "GUEST") {
            userRoleChange = "게스트";
        } else if (userRole == "SELLER") {
            userRoleChange = "판매자";
        } else if (userRole == "HOST") {
            userRoleChange = "호스트";
        }

        String message = "\n역할: " + userRoleChange + "\nId: " + userPk + "\n닉네임: " + username;
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
        } catch (Exception ex) {
            log.error("Error sending login data to Discord: " + ex.getMessage());
        }
    }




    @Value("${discord.productWebhookUrl:}") // Load value from application.yml
    private String discordProductWebhookUrl;
//    private final String discordProductWebhookUrl2 = "https://discord.com/api/webhooks/1142067860441669682/d0C9vouv8X9Nb7acu1SBxcMwTgfDkMUXbEyDPPwS-6485fQqcVqH-1_rR2nqFfpAFWpd";
    public void sendProductMessage(Product product) {
        if (discordProductWebhookUrl.isEmpty()) {
            return;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> imageEmbed = new HashMap<>();
            String message = product.getSeller().getName()+
                    " 님이 상품을 등록했습니다\n\n상품번호: " +product.getId()+
                    "\n상품명:"+product.getName()+
                    "\n상품카테고리: "+product.getSubCategory().getCategory().getName()+
                    "-- "+product.getSubCategory().getName()+
                    "\n상품 이미지:";
            imageEmbed.put("title", "상품등록");
            imageEmbed.put("description", message);
            imageEmbed.put("color", 8388736);
            boolean imgCheck=true;
            String urlData = product.getImgUrl();
            try {
                String encodedPart = "";
                String[] urlParts = urlData.split("/");
                String dto;
                String lastPart = urlParts[urlParts.length - 1];
                for(char charAt : lastPart.toCharArray()){
                    if(Character.isWhitespace(charAt)){
                        encodedPart+="%20";
                    }else if(0x20 > charAt || charAt > 0x7E){

                        encodedPart+=URLEncoder.encode(String.valueOf(charAt), "UTF-8");
                    }else{
                        encodedPart+=String.valueOf(charAt);
                    }
                }
//                encodedPart = URLEncoder.encode(lastPart, "UTF-8");
                urlData = urlData.replace(lastPart, encodedPart);
                URL url = new URL(urlData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");

            } catch (Exception e) {
                imgCheck=false;
            }
            if(imgCheck==true){
                log.info("url={}",urlData);
                String finalUrlData = urlData;
                imageEmbed.put("image", new HashMap<String, Object>() {{
                    put("url", finalUrlData);
                }});
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("embeds", new Object[]{imageEmbed});

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    discordProductWebhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (Exception ex) {
            log.error("Error sending product data to Discord: " + ex.getMessage());
        }
    }
}





