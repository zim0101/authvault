package com.zim0101.authvault.service;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthUserService {
    private final AccountRepository accountRepository;

    public AuthUserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Account currentAuthAccount() {
        return accountRepository.findByUsername(getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    public boolean authUserIsAdminGroup() {
        Authentication authentication = getAuthentication();
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        List<String> roles = List.of("ROLE_ADMIN");

        return userRoles.stream().anyMatch(roles::contains);
    }

}

