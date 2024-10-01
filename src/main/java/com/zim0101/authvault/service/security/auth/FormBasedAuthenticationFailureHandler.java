package com.zim0101.authvault.service.security.auth;

import com.zim0101.authvault.exception.EmailNotVerifiedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FormBasedAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception.getCause() instanceof EmailNotVerifiedException) {
            errorMessage = "Please verify your email before logging in";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else {
            errorMessage = "An error occurred during login: " + exception.getMessage();
        }

        request.getSession().setAttribute("error", errorMessage);

        response.sendRedirect("/login?error");
    }
}