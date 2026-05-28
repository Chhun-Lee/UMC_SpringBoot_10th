package com.example.umc10th.global.security.oauth;

import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.member.enums.Gender;
import com.example.umc10th.domain.member.enums.SocialType;
import com.example.umc10th.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException("Unsupported OAuth provider: " + registrationId);
        }

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.from(oauth2User.getAttributes());
        Member member = getOrCreateMember(kakaoUserInfo);

        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("memberId", member.getId());
        attributes.put("email", member.getEmail());
        attributes.put("name", member.getName());
        attributes.put("provider", SocialType.KAKAO.name());
        attributes.put("providerId", kakaoUserInfo.providerId());

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }

    private Member getOrCreateMember(KakaoUserInfo kakaoUserInfo) {
        return memberRepository.findByProviderAndProviderId(SocialType.KAKAO, kakaoUserInfo.providerId())
                .or(() -> memberRepository.findByEmail(kakaoUserInfo.email()))
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .name(truncate(kakaoUserInfo.nickname(), 20))
                        .email(kakaoUserInfo.email())
                        .gender(Gender.UNKNOWN)
                        .provider(SocialType.KAKAO)
                        .providerId(kakaoUserInfo.providerId())
                        .profile(kakaoUserInfo.profileImageUrl())
                        .point(0)
                        .build()));
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.isBlank()) {
            return "kakao_user";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private record KakaoUserInfo(
            String providerId,
            String email,
            String nickname,
            String profileImageUrl
    ) {

        static KakaoUserInfo from(Map<String, Object> attributes) {
            String providerId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = getMap(attributes.get("kakao_account"));
            Map<String, Object> profile = getMap(kakaoAccount.get("profile"));

            String email = (String) kakaoAccount.get("email");
            if (email == null || email.isBlank()) {
                throw new OAuth2AuthenticationException("Kakao account email is required");
            }

            return new KakaoUserInfo(
                    providerId,
                    email,
                    (String) profile.get("nickname"),
                    (String) profile.get("profile_image_url")
            );
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> getMap(Object value) {
            if (value instanceof Map<?, ?> map) {
                return (Map<String, Object>) map;
            }
            return Map.of();
        }
    }
}
