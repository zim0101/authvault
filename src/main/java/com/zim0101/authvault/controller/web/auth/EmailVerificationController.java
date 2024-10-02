package com.zim0101.authvault.controller.web.auth;

import com.zim0101.authvault.exception.VerificationTokenDoesNotExistException;
import com.zim0101.authvault.exception.VerificationTokenExpiredException;
import com.zim0101.authvault.service.business.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmailVerificationController {

    Logger logger = LoggerFactory.getLogger(EmailVerificationController.class);

    private final AccountService accountService;

    public EmailVerificationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/email-verification")
    public String emailVerification(@RequestParam("token") String token,
                                    RedirectAttributes redirectAttributes) {
        try {
            accountService.markEmailAsVerified(token);
            redirectAttributes.addFlashAttribute("emailVerifiedSuccessful",
                    "Your email has been successfully verified. You can now log in.");
            return "redirect:/login";
        } catch (VerificationTokenDoesNotExistException exception) {
            return "redirect:/bad-token";
        } catch (VerificationTokenExpiredException exception) {
            return "redirect:/expired-token";
        }
    }

    @GetMapping("bad-token")
    public String badToken(@RequestParam("token") String token) {
        return "error/bad_token";
    }

    @GetMapping("expired-token")
    public String expiredToken(@RequestParam("token") String token) {
        return "error/expired_token";
    }
}