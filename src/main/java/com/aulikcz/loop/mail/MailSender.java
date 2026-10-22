package com.aulikcz.loop.mail;

public interface MailSender {
    void sendPasswordReset(String toEmail, String resetLink);
}
