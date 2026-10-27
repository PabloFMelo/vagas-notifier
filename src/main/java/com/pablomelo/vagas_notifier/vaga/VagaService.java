package com.pablomelo.vagas_notifier.vaga;

import com.pablomelo.vagas_notifier.github.GitHubIssue;
import com.pablomelo.vagas_notifier.github.GitHubService;
import com.pablomelo.vagas_notifier.telegram.TelegramService;
import org.springframework.stereotype.Service;

@Service
public class VagaService {

    private final GitHubService gitHubService;
    private final TelegramService telegramService;
    private final VagaRepository vagaRepository;

    public VagaService(GitHubService gitHubService,
                       TelegramService telegramService,
                       VagaRepository vagaRepository) {
        this.gitHubService = gitHubService;
        this.telegramService = telegramService;
        this.vagaRepository = vagaRepository;
    }

    public void processarNovasVagas() {
        for (GitHubIssue issue : gitHubService.buscarVagas()) {
            if (vagaRepository.existsByGithubId(issue.id())) {
                continue;
            }

            telegramService.enviarMensagem(issue.title() + "\n" + issue.url());

            vagaRepository.save(new Vaga(issue.id(), issue.title(), issue.url(), issue.criadaEm()));
        }
    }
}
