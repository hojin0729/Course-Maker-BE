//package coursemaker.coursemaker.oauth.service;
//
//import coursemaker.coursemaker.domain.member.entity.Member;
//import coursemaker.coursemaker.domain.member.repository.MemberRepository;
//import coursemaker.coursemaker.oauth.dto.CustomOAuth2User;
//import coursemaker.coursemaker.oauth.dto.KakaoResponse;
//import coursemaker.coursemaker.oauth.dto.OAuth2Response;
//import coursemaker.coursemaker.oauth.dto.OAuthAccountDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // 부모 클래스의 메서드를 사용하여 객체를 생성함.
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        System.out.println(oAuth2User);
//        // 제공자
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        OAuth2Response oAuth2Response = null;
//        if ("kakao".equals(registrationId)) {
//            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
//        } else {
//            return null;
//        }
//
//        // 사용자명을 제공자_회원아이디 형식으로만들어 저장
//        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
//        Member existData = memberRepository.findByUsername(username);
//
//        if (existData == null) {
//
//            Member userEntity = new Member();
//            userEntity.setUsername(username);
//            userEntity.setEmail(oAuth2Response.getEmail());
//            userEntity.setName(oAuth2Response.getName());
//            userEntity.setNickname(oAuth2Response.getName()); // 닉네임 설정
//            userEntity.setRoles("ROLE_USER");
//
//            memberRepository.save(userEntity);
//
//            OAuthAccountDto userDTO = new OAuthAccountDto();
//            userDTO.setUsername(username);
//            userDTO.setName(oAuth2Response.getName());
//            userDTO.setRoles("ROLE_USER");
//
//            return new CustomOAuth2User(userDTO);
//        }else {
//            // 회원정보가 존재한다면 조회된 데이터로 반환
//            existData.setEmail(oAuth2Response.getEmail());
//            existData.setName(oAuth2Response.getName());
//            existData.setNickname(oAuth2Response.getName()); // 닉네임 설정
//
//
//            memberRepository.save(existData);
//
//            OAuthAccountDto userDTO = new OAuthAccountDto();
//            userDTO.setUsername(existData.getUsername());
//            userDTO.setName(oAuth2Response.getName());
//            userDTO.setRoles(existData.getRoles());
//
//            return new CustomOAuth2User(userDTO);
//        }
//    }
//}