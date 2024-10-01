package com.zim0101.authvault.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.Arrays;

@Configuration
@EnableJms
public class JmsConfiguration {
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        activeMQConnectionFactory.setUserName(username);
        activeMQConnectionFactory.setPassword(password);
        activeMQConnectionFactory.setTrustAllPackages(false);
        activeMQConnectionFactory.setTrustedPackages(Arrays.asList("com.zim0101.authvault.model", "com.zim0101.authvault.dto", "java.lang", "java.util"));

        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);

        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(cachingConnectionFactory());
    }
}