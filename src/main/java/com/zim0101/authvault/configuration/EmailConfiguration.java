package com.zim0101.authvault.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Value("${spring.mail.debug}")
    private String mailDebug;


    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(port)));
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        javaMailProperties.put("mail.smtp.auth", mailSmtpAuth);
        javaMailProperties.put("mail.transport.protocol", mailProtocol);
        javaMailProperties.put("mail.debug", mailDebug);
        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}