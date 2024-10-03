package com.zim0101.authvault.service.security.oauth2;

import com.zim0101.authvault.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    private final Map<String, OAuth2UserHandler> oauthUserHandlers;

    public OAuthAuthenticationSuccessHandler(Map<String, OAuth2UserHandler> oauthUserHandlers) {
        this.oauthUserHandlers = oauthUserHandlers;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        OAuth2User user = oAuth2AuthenticationToken.getPrincipal();

        if (authorizedClientRegistrationId == null) {
            logger.error("Authorized client is null");
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Authorized client is null", null));
        }

        OAuth2UserHandler handler = oauthUserHandlers.get(authorizedClientRegistrationId.toLowerCase());
        if (handler == null) {
            logger.error("Unsupported OAuth provider: {}", authorizedClientRegistrationId);
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Unsupported OAuth provider", null));
        }

        try {
            handler.handleUser(user);
        } catch (Exception e) {
            logger.error("Error handling OAuth user", e);
            throw new OAuth2AuthenticationException(new OAuth2Error("user_processing_error", "Error processing user data", null));
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }
}