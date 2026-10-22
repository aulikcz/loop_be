package com.aulikcz.loop.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "mail.provider", havingValue = "brevo")
public class BrevoMailSender implements MailSender {

    private final RestClient client;
    private final String from;

    public BrevoMailSender(@Value("${brevo.api-key}") String apiKey,
                           @Value("${mail.from}") String from) {
        this.from = from;
        this.client = RestClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .build();
    }

    @Override
    public void sendPasswordReset(String to, String url) {
        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "Loop", "email", from),
                "to", List.of(Map.of("email", to)),
                "subject", "Nastavení nového hesla",
                "htmlContent", "<p>Pro nastavení nového hesla klikni na odkaz (platí 30 minut):</p>"
                        + "<p><a href=\"" + url + "\">" + url + "</a></p>");
        try {
            client.post().uri("/smtp/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Odeslání e-mailu selhalo", e);
        }
    }
}
