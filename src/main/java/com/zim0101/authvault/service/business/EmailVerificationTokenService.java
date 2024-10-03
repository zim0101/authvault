package com.zim0101.authvault.service.business;

import com.zim0101.authvault.exception.VerificationTokenDoesNotExistException;
import com.zim0101.authvault.exception.VerificationTokenExpiredException;
import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.EmailVerificationToken;
import com.zim0101.authvault.repository.EmailVerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    Logger logger = LoggerFactory.getLogger(EmailVerificationTokenService.class);

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final ToastMessageService toastMessageService;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository,
                                         ToastMessageService toastMessageService) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.toastMessageService = toastMessageService;
    }

    private String generateEmailVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        emailVerificationTokenRepository.save(verificationToken);

        return token;
    }

    public Account getAccountFromValidVerificationEmailToken(String token) throws
            VerificationTokenDoesNotExistException,
            VerificationTokenExpiredException {

        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByToken(token)
                .orElseThrow(VerificationTokenDoesNotExistException::new);

        boolean isExpired = verificationToken.getExpiryDate().isBefore(LocalDateTime.now());

        if (isExpired) {
            String toastMessage = toastMessageService
                    .getLocalizedErrorMessage("error.email.verification_token_expired");
            throw new VerificationTokenExpiredException(toastMessage);
        }

        return verificationToken.getAccount();
    }

    public String getEmailVerificationUrl(Account account) {
        String token = generateEmailVerificationToken(account);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();

        return baseUrl + "/email-verification?token=" + token;
    }
}