package com.zim0101.authvault.service.business;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.AuthProvider;
import com.zim0101.authvault.model.enums.Role;
import com.zim0101.authvault.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.Set;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
    public String register(Account account, BindingResult result) {
        if (result.hasErrors()) return "auth/register";

        if (accountExistWithEmail(account.getEmail())) {
            result.rejectValue("email", "duplicate.email",
                    "There is already an account registered with the same email");
            return "auth/register";
        }

        saveAccount(account);

        emailService.sendVerificationEmail(account.getEmail());

        return "redirect:/login";
    }
}
