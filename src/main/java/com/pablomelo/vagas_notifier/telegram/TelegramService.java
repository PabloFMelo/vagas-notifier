package com.pablomelo.vagas_notifier.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TelegramService {

    private final RestClient restClient;
    private final String chatId;

    public TelegramService(@Value("${telegram.bot.token}") String token,
                           @Value("${telegram.chat.id}") String chatId) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token)
                .build();
        this.chatId = chatId;
    }

    public void enviarMensagem(String texto) {
        restClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("chat_id", chatId, "text", texto))
                .retrieve()
                .toBodilessEntity();
    }
}
