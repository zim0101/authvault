package com.zim0101.authvault.service.business;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ToastMessageService {
    private final MessageSource messageSource;

    public ToastMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLocalizedErrorMessage(String errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, null, locale);
    }
}