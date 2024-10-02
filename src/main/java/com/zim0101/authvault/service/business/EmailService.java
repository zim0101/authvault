package com.zim0101.authvault.service.business;

import com.zim0101.authvault.dto.EmailDto;
import com.zim0101.authvault.service.activemq.publisher.EmailVerificationJobPublisher;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${jmspoc.email.default.display-name}")
    private String defaultDisplayName;

    @Value("${jmspoc.email.default.sender-address}")
    private String defaultSenderAddress;

    private final JavaMailSender mailSender;

    private final EmailVerificationJobPublisher emailVerificationJobPublisher;

    public EmailService(JavaMailSender mailSender, EmailVerificationJobPublisher emailVerificationJobPublisher) {
        this.mailSender = mailSender;
        this.emailVerificationJobPublisher = emailVerificationJobPublisher;
    }

    public void sendMail(EmailDto email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject(email.getMailSubject());
            mailMessage.setFrom(String.valueOf(new InternetAddress(
                    email.getMailFrom() != null ? email.getMailFrom() : defaultSenderAddress,
                    email.getDisplayName() != null ? email.getDisplayName() : defaultDisplayName)));
            mailMessage.setTo(email.getMailTo());
            mailMessage.setText(email.getMailContent());
            mailSender.send(mailMessage);
        } catch (MailException | UnsupportedEncodingException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public void sendVerificationEmail(String receiverEmail, String emailVerificationUrl) {
        EmailDto emailDto = new EmailDto();
        emailDto.setMailTo(receiverEmail);
        emailDto.setMailSubject("Verify Your Email");
        emailDto.setMailContent("Click here to verify your email: " + emailVerificationUrl);

        emailVerificationJobPublisher.publishMessage(emailDto);
    }
}