package com.zim0101.authvault.service;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.Role;
import com.zim0101.authvault.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Set;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account findById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Account not found!"));
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public boolean accountExistWithEmail(String email) {
        return findByEmail(email) != null;
    }

    @Transactional
    public void saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRoles() == null) {
            account.setRoles(Set.of(Role.USER));
        }
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

        return "redirect:/login";
    }
}
