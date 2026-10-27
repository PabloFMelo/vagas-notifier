package com.pablomelo.vagas_notifier;

import com.pablomelo.vagas_notifier.vaga.VagaService;
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
	CommandLineRunner processarVagas(VagaService vagaService) {
		return args -> vagaService.processarNovasVagas();
	}

}