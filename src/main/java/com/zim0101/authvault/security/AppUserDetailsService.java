package com.zim0101.authvault.security;

import com.zim0101.authvault.exception.EmailNotVerifiedException;
import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.repository.AccountRepository;
import com.zim0101.authvault.service.business.ToastMessageService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final ToastMessageService toastMessageService;

    public AppUserDetailsService(AccountRepository accountRepository,
                                 ToastMessageService toastMessageService) {
        this.accountRepository = accountRepository;
        this.toastMessageService = toastMessageService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException,
            EmailNotVerifiedException {
        Account account = accountRepository.findByUsername(usernameOrEmail)
                .or(() -> accountRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User " + usernameOrEmail + "is not found"));

        if (!account.getEmailVerified()) {
            String toastMessage = toastMessageService.getLocalizedErrorMessage("error.email.not_verified");
            throw new EmailNotVerifiedException(toastMessage);
        }

        String[] roles = account.getRoles()
                .stream().map(Enum::name).toArray(String[]::new);

        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(roles)
                .build();
    }
}
