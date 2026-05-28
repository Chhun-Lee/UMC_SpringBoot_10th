package com.example.umc10th.global.security.oauth;

import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.global.apiPayload.CustomResponse;
import com.example.umc10th.global.code.status.MemberSuccessCode;
import com.example.umc10th.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        Long memberId = Long.valueOf(String.valueOf(oauth2User.getAttribute("memberId")));

        UserDetails userDetails = User.builder()
                .username(email)
                .password("")
                .authorities(authentication.getAuthorities())
                .build();

        String accessToken = jwtUtil.createAccessToken(userDetails);
        MemberResDTO.LoginResultDTO result = MemberResDTO.LoginResultDTO.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();

        authorizationRequestRepository.removeAuthorizationRequestCookies(response);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(),
                CustomResponse.successBody(MemberSuccessCode.MEMBER_LOGIN_OK, result));
    }
}
