package com.zim0101.authvault.service.business;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.AuthProvider;
import com.zim0101.authvault.model.enums.Role;
import com.zim0101.authvault.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          EmailVerificationTokenService emailVerificationTokenService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    public boolean accountExistWithEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthProvider(AuthProvider.LOCAL);

        if (account.getRoles() == null) {
            account.setRoles(Set.of(Role.USER));
        }

        accountRepository.save(account);
    }

    @Transactional
    public void saveOrUpdateSocialAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public String register(Account account, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "auth/register";

        if (accountExistWithEmail(account.getEmail())) {
            result.rejectValue("email", "duplicate.email",
                    "There is already an account registered with the same email");
            return "auth/register";
        }

        saveAccount(account);

        String emailVerificationUrl = emailVerificationTokenService.getEmailVerificationUrl(account);
        emailService.sendVerificationEmail(account.getEmail(), emailVerificationUrl);

        redirectAttributes.addFlashAttribute("verificationEmailSent", "A verification email has been sent to your " +
                "email address. Please check your inbox.");

        return "redirect:/login";
    }

    @Transactional
    public void markEmailAsVerified(String token) {
        Account account = emailVerificationTokenService.getAccountFromValidVerificationEmailToken(token);

        logger.info("Account: {}", account);

        account.setEmailVerified(true);
        accountRepository.save(account);
    }
}
