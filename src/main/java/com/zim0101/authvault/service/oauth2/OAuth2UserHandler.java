package com.zim0101.authvault.service.oauth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserHandler {
    void handleUser(OAuth2User oAuth2User) throws OAuth2AuthenticationException;
}
