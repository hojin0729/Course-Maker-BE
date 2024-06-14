package coursemaker.coursemaker.oauth;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Component
@Getter
public class KakaoOauth {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoApiKey;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret; // 추가: client_secret 값


    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoLoginRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.kakao.logout-redirect-uri}")
    private String kakaoLogoutRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.expire-token-uri}")
    private String expireKakaoTokenUri;

    public String getKakaoAccessToken(String code) {
        // 액세스 토큰을 저장할 변수 초기화
        String kakaoAccessToken = "";

        try{
            // 카카오 토큰 요청 URL 생성
            URL url = new URL(kakaoTokenUri);
            // URL 연결 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // HTTP 요청 헤더 설정
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            //HTTP 요청 바디 작성
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //카카오 API에 필요한 파라미터들을 설정
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoApiKey);
            sb.append("&client_secret=").append(kakaoClientSecret); // 추가: client_secret 값
            sb.append("&redirect_uri=").append(kakaoLoginRedirectUri);
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            //응답 코드 확인
            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);


            //응답 읽기
            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }

            //JSON 파싱
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonElement element = JsonParser.parseString(result);

            JsonObject jsonObject = element.getAsJsonObject();


            //액세스 토큰 추출
            if (jsonObject != null && jsonObject.has("access_token")) {
                kakaoAccessToken = jsonObject.get("access_token").getAsString();
            } else {
                log.error("Invalid response: accessToken을 찾을 수 없습니다.");
                throw new RuntimeException("Invalid response: accessToken을 찾을 수 없습니다.");
            }

            br.close();
            bw.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return kakaoAccessToken;
    }

    public HashMap<String, Object> getUserInfoFromKakaoToken(String kakaoAccessToken) {
        // 사용자 정보를 저장할 HashMap 초기화
        HashMap<String, Object> userInfo = new HashMap<>();
        try{
            // 사용자 정보 요청 URL 생성
            URL url = new URL(userInfoUri);
            // URL 연결 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // HTTP 요청 메서드 설정
            conn.setRequestMethod("POST");
            // HTTP 요청 헤더 설정: Authorization 및 Content-type
            conn.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

            // 응답 데이터를 읽기 위한 BufferedReader 생성
            BufferedReader br;
            // 응답 코드가 200-300 사이일 경우 입력 스트림을 사용
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else { //// 그렇지 않을 경우 에러 스트림을 사용
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            // 응답 데이터를 읽기 위해 StringBuilder 사용
            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }

            // 응답 결과를 String으로 변환
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            // JSON 형식의 응답 결과 파싱
            JsonElement element = JsonParser.parseString(result);

            // properties 객체에서 닉네임 추출
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            String id = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();

            // 사용자 정보를 HashMap에 저장
            userInfo.put("id", id);
            userInfo.put("nickname", nickname);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public void expireKakaoToken(String kakaoAccessToken) {

        try{
            URL url = new URL(expireKakaoTokenUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.kakaoLogout] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("kakao logout - responseBody = {}", result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

