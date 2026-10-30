package com.pablomelo.vagas_notifier.telegram;

import com.pablomelo.vagas_notifier.notificacao.Notificador;
import com.pablomelo.vagas_notifier.vaga.Vaga;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TelegramNotificador implements Notificador {

    private final RestClient restClient;
    private final String chatId;

    public TelegramNotificador(@Value("${telegram.bot.token}") String token,
                               @Value("${telegram.chat.id}") String chatId) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token)
                .build();
        this.chatId = chatId;
    }
    @Override
    @Retryable(
            includes = {
                    ResourceAccessException.class,
                    HttpServerErrorException.class,
                    HttpClientErrorException.TooManyRequests.class
            },
            maxRetries = 3,
            delay = 2000,
            multiplier = 2
    )

    public void notificar(Vaga vaga) {
        restClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "chat_id", chatId,
                        "text", formatar(vaga),
                        "parse_mode", "HTML"
                ))
                .retrieve()
                .toBodilessEntity();
    }

    private String formatar(Vaga vaga) {
        return "💼 <b>" + escaparHtml(vaga.getTitulo()) + "</b>\n\n"
                + "🔗 <a href=\"" + vaga.getUrl() + "\">Ver vaga</a>";
    }

    private String escaparHtml(String texto) {
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
