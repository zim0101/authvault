package com.zim0101.authvault.service.oauth2;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.AuthProvider;
import com.zim0101.authvault.model.enums.Role;
import com.zim0101.authvault.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component("github")
public class GithubUserHandler implements OAuth2UserHandler {

    Logger logger = LoggerFactory.getLogger(GithubUserHandler.class);

    private final AccountService accountService;

    public GithubUserHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handleUser(OAuth2User oAuth2User) throws OAuth2AuthenticationException {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String id = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();

        Account account = accountService.findByEmail(email);
        if (account == null) {
            account = new Account();
            account.setEmail(email);
            account.setUsername(email);
            account.setRoles(Collections.singleton(Role.USER));
        }
        account.setName(name);
        account.setAuthProvider(AuthProvider.GITHUB);
        account.setAuthProviderId(id);



        accountService.saveOrUpdateSocialAccount(account);
    }
}