//package coursemaker.coursemaker.oauth.dto;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//public class CustomOAuth2User implements OAuth2User {
//    private final OAuthAccountDto oAuthAccountDto;
//
//    public CustomOAuth2User(OAuthAccountDto oAuthAccountDto) {
//        this.oAuthAccountDto = oAuthAccountDto;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return null;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//
//                return oAuthAccountDto.getRoles();
//            }
//        });
//
//        return collection;
//    }
//
//    @Override
//    public String getName() {
//        return oAuthAccountDto.getName();
//    }
//
//    public String getUsername() {
//        return oAuthAccountDto.getUsername();
//    }
//}