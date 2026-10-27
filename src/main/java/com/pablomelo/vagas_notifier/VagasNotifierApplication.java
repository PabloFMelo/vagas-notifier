package com.pablomelo.vagas_notifier;

import com.pablomelo.vagas_notifier.github.GitHubIssue;
import com.pablomelo.vagas_notifier.github.GitHubService;
import com.pablomelo.vagas_notifier.telegram.TelegramService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VagasNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(VagasNotifierApplication.class, args);
	}

	@Bean
	CommandLineRunner enviarVagas(GitHubService gitHubService, TelegramService telegramService) {
		return args -> {
			List<GitHubIssue> vagas = gitHubService.buscarVagas();

			for (GitHubIssue vaga : vagas) {
				telegramService.enviarMensagem(vaga.title() + "\n" + vaga.url());
			}
		};
	}

}