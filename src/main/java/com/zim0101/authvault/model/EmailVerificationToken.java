package com.zim0101.authvault.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class EmailVerificationToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_token_generator")
    @SequenceGenerator(name = "email_verification_token_generator", sequenceName = "email_verification_token_seq")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    private String token;

    private LocalDateTime expiryDate;

    public EmailVerificationToken() {}

    public EmailVerificationToken(Long id, Account account, String token, LocalDateTime expiryDate) {
        this.id = id;
        this.account = account;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "EmailVerificationToken{" +
                "id=" + id +
                ", token=" + token +
                ", expiryDate=" + expiryDate +
                "}";
    }
}