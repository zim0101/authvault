package com.zim0101.authvault.service.security.auth;

import com.zim0101.authvault.model.enums.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FormBasedAuthenticationHandler implements AuthenticationSuccessHandler {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_ADMIN = ROLE_PREFIX + Role.ADMIN.name();
    private static final String ROLE_USER = ROLE_PREFIX + Role.USER.name();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectURL = determineRedirectUrl(authentication.getAuthorities());

        if ("/".equals(redirectURL)) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "You do not have sufficient privileges to access any dashboard.");
        }

        response.sendRedirect(redirectURL);
    }

    private String determineRedirectUrl(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains(ROLE_ADMIN)) {
            return "/admin/dashboard";
        } else if (roles.contains(ROLE_USER)) {
            return "/user/dashboard";
        } else {
            return "/";
        }
    }
}
