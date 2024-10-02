package com.zim0101.authvault.service.security.auth;

import com.zim0101.authvault.exception.EmailNotVerifiedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FormBasedAuthenticationFailureHandler implements AuthenticationFailureHandler {

    Logger logger = LoggerFactory.getLogger(FormBasedAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception.getCause() instanceof EmailNotVerifiedException) {
            logger.info("unverifiedEmailLoginError");
            errorMessage = "Please verify your email before logging in";
        } else if (exception instanceof BadCredentialsException) {
            logger.info("badCredentialsError");
            errorMessage = "Bad credentials. Invalid username or password";
        } else if (exception instanceof UsernameNotFoundException) {
            logger.info("UsernameNotFoundException");
            errorMessage = "Invalid username or password";
        } else {
            logger.info("exception");
            errorMessage = "An error occurred during login: " + exception.getMessage();
        }

        request.getSession().setAttribute("FLASH_ERROR_MESSAGE", errorMessage);

        response.sendRedirect("/login?error");
    }
}