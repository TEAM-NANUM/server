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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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




//    @Value("${discord.productWebhookUrl:}") // Load value from application.yml
//    private String discordProductWebhookUrl;
//        private final String discordProductWebhookUrl2 = "https://discord.com/api/webhooks/1142067860441669682/d0C9vouv8X9Nb7acu1SBxcMwTgfDkMUXbEyDPPwS-6485fQqcVqH-1_rR2nqFfpAFWpd";
//    public void sendProductMessage(Product product) {
//        if (discordProductWebhookUrl2.isEmpty()) {
//            return;
//        }
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            Map<String, Object> imageEmbed = new HashMap<>();
//            String message = product.getSeller().getName()+
//                    " 님이 상품을 등록했습니다\n\n상품번호: " +product.getId()+
//                    "\n상품명:"+product.getName()+
//                    "\n상품카테고리: "+product.getSubCategory().getCategory().getName()+
//                    "-- "+product.getSubCategory().getName()+
//                    "\n상품 이미지:";
//            imageEmbed.put("title", "상품등록");
//            imageEmbed.put("description", message);
//            imageEmbed.put("color", 8388736);
//            boolean imgCheck=true;
//            String url1="https://kr.object.ncloudstorage.com/nanum-bucket/20230818221801_그림5";
//            String url1="https://kr.object.ncloudstorage.com/nanum-bucket/20230818221801_%EA%B7%B8%EB%A6%BC5";
//            String completeUrl = null;
//            try {
//                String[] dtoOne = product.getImgUrl().split("/");
//                String[] dtoTwo = dtoOne[dtoOne.length-1].split("_");
//                completeUrl = dtoTwo[0]+"_";
//                String koreanUrl=null;
//                for(int i=1;i<dtoTwo.length;i++){
//                    koreanUrl+=dtoTwo[i];
//                }
//                String encodedText = URLEncoder.encode(koreanUrl, "UTF-8");
//                completeUrl+=encodedText;
//                URL url = new URL(completeUrl);
//
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("HEAD");
//
//            } catch (Exception e) {
//                imgCheck=false;
//            }
//            if(imgCheck==true){
//                String finalCompleteUrl = completeUrl;
//                imageEmbed.put("image", new HashMap<String, Object>() {{
//                    put("url", finalCompleteUrl);
//                }});
//            }
//
//            Map<String, Object> payload = new HashMap<>();
//            payload.put("embeds", new Object[]{imageEmbed});
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    discordProductWebhookUrl2,
//                    HttpMethod.POST,
//                    entity,
//                    String.class
//            );
//        } catch (Exception ex) {
//            log.error("Error sending login data to Discord: " + ex.getMessage());
//        }
//    }
}





