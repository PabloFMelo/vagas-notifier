package com.pablomelo.vagas_notifier.vaga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VagaScheduler {

    private static final Logger log = LoggerFactory.getLogger(VagaScheduler.class);

    private final VagaService vagaService;

    public VagaScheduler(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @Scheduled(fixedDelayString = "${vagas.intervalo-busca}")
    public void executar() {
        log.info("Iniciando busca de novas vagas...");
        try {
            vagaService.processarNovasVagas();
        } catch (Exception e) {
            log.error("Falha na busca de vagas. Nova tentativa no próximo ciclo.", e);
        }
    }
}
