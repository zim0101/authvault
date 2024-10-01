package com.zim0101.authvault.service.activemq.subscriber;

import com.zim0101.authvault.dto.EmailDto;
import com.zim0101.authvault.service.business.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationJobSubscriber {

    private static final Logger log = LoggerFactory.getLogger(EmailVerificationJobSubscriber.class);
    private final EmailService emailService;

    public EmailVerificationJobSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "${authvault.queue.email-verification}")
    public void subscribeMessage(EmailDto emailDto) {
        log.info("EmailVerificationJobSubscriber received");
        emailService.sendMail(emailDto);
    }
}