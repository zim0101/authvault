package com.zim0101.authvault.security.oauth2;

import com.zim0101.authvault.service.business.ToastMessageService;
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
    private final ToastMessageService toastMessageService;

    public OAuthAuthenticationSuccessHandler(Map<String, OAuth2UserHandler> oauthUserHandlers,
                                             ToastMessageService toastMessageService) {
        this.oauthUserHandlers = oauthUserHandlers;
        this.toastMessageService = toastMessageService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String toastMessage;

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        OAuth2User user = oAuth2AuthenticationToken.getPrincipal();

        if (authorizedClientRegistrationId == null) {
            toastMessage = toastMessageService.getLocalizedErrorMessage("error.oauth2_client_is_null");
            throw new OAuth2AuthenticationException(toastMessage);
        }

        OAuth2UserHandler handler = oauthUserHandlers.get(authorizedClientRegistrationId.toLowerCase());
        if (handler == null) {
            toastMessage = toastMessageService.getLocalizedErrorMessage("error.oauth2_unsupported_provider");
            throw new OAuth2AuthenticationException(toastMessage);
        }

        try {
            handler.handleUser(user);
        } catch (Exception e) {
            toastMessage = toastMessageService.getLocalizedErrorMessage("error.oauth2_user_processing_error");
            throw new OAuth2AuthenticationException(toastMessage);
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }
}