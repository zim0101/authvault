package com.zim0101.authvault.service.business;

import com.zim0101.authvault.exception.VerificationTokenDoesNotExistException;
import com.zim0101.authvault.exception.VerificationTokenExpiredException;
import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.EmailVerificationToken;
import com.zim0101.authvault.repository.EmailVerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    Logger logger = LoggerFactory.getLogger(EmailVerificationTokenService.class);

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
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

    public Account getAccountFromValidVerificationEmailToken(String token) throws VerificationTokenDoesNotExistException,
            VerificationTokenExpiredException {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByToken(token)
                .orElseThrow(VerificationTokenDoesNotExistException::new);
        logger.info("EmailVerificationToken: {}", verificationToken);
        boolean isExpired = verificationToken.getExpiryDate().isBefore(LocalDateTime.now());
        logger.info("isExpired: {}", isExpired);
        if (isExpired) {
            throw new VerificationTokenExpiredException();
        }
        logger.info("Is valid!!!!!!!!!");

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