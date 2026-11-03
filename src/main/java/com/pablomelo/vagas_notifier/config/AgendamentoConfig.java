package com.pablomelo.vagas_notifier.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "vagas.agendamento.habilitado", havingValue = "true", matchIfMissing = true)
public class AgendamentoConfig {
}
