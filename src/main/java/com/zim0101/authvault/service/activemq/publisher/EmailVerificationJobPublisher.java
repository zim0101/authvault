package com.zim0101.authvault.service.activemq.publisher;

import com.zim0101.authvault.dto.EmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationJobPublisher {
    @Value("${authvault.queue.email-verification}")
    private String destination;

    private final JmsTemplate jmsTemplate;

    public EmailVerificationJobPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publishMessage(EmailDto emailDto) {
        jmsTemplate.convertAndSend(destination, emailDto);
    }
}