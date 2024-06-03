package coursemaker.coursemaker.oauth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.get("has_email") == Boolean.TRUE) {
            return String.valueOf(kakaoAccount.get("email"));
        }
        return null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {
            return String.valueOf(properties.get("nickname"));
        }
        return null;
    }
}
