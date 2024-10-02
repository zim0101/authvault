package com.zim0101.authvault.controller.web.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping
    public String login(HttpServletRequest request, Model model) {
        String loginErrorMessage = (String) request.getSession().getAttribute("FLASH_ERROR_MESSAGE");

        if (loginErrorMessage != null) {
            model.addAttribute("loginErrorMessage", loginErrorMessage);
            logger.info("loginErrorMessage: {}", loginErrorMessage);
            request.getSession().removeAttribute("FLASH_ERROR_MESSAGE");
        }

        return "auth/login";
    }
}
