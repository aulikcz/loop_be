package com.aulikcz.loop.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "mail.provider", havingValue = "log", matchIfMissing = true)
public class LoggingMailSender implements MailSender {
    @Override
    public void sendPasswordReset(String toEmail, String resetLink) {
        log.info("Reset password: {} to email {}", resetLink, toEmail);
    }
}
