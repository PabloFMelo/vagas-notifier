package com.pablomelo.vagas_notifier;

import com.pablomelo.vagas_notifier.telegram.TelegramService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VagasNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(VagasNotifierApplication.class, args);
	}

	@Bean
	CommandLineRunner testarTelegram(TelegramService telegramService) {
		return args -> telegramService.enviarMensagem("Olá! Meu primeiro bot está funcionando 🚀");
	}
}
