package com.pablomelo.vagas_notifier.vaga;

import com.pablomelo.vagas_notifier.filtro.FiltroVagas;
import com.pablomelo.vagas_notifier.github.GitHubIssue;
import com.pablomelo.vagas_notifier.github.GitHubService;
import com.pablomelo.vagas_notifier.notificacao.Notificador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VagaService {

    private final GitHubService gitHubService;
    private final Notificador notificador;
    private final VagaRepository vagaRepository;
    private final FiltroVagas filtroVagas;

    public VagaService(GitHubService gitHubService,
                       Notificador notificador,
                       VagaRepository vagaRepository,
                       FiltroVagas filtroVagas) {
        this.gitHubService = gitHubService;
        this.notificador = notificador;
        this.vagaRepository = vagaRepository;
        this.filtroVagas = filtroVagas;
    }

    private static final Logger log = LoggerFactory.getLogger(VagaService.class);

    public void processarNovasVagas() {
        int enviadas = 0;
        int falhas = 0;

        for (GitHubIssue issue : gitHubService.buscarVagas()) {
            if (vagaRepository.existsByGithubId(issue.id())) {
                continue;
            }

            if (!filtroVagas.aceita(issue)) {
                continue;
            }

            Vaga vaga = new Vaga(issue.id(), issue.title(), issue.url(), issue.criadaEm());

            try {
                notificador.notificar(vaga);
                vagaRepository.save(vaga);
                enviadas++;
            } catch (Exception e) {
                log.error("Falha ao notificar a vaga '{}'. Ela será tentada na próxima busca.", vaga.getTitulo(), e);
                falhas++;
            }
        }

        log.info("Busca concluída: {} vaga(s) enviada(s), {} falha(s)", enviadas, falhas);
    }
}

