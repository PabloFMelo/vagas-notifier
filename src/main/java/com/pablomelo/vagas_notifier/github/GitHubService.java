package com.pablomelo.vagas_notifier.github;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GitHubService {
    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader("Accept", "application/vnd.github+json")
            .build();

    public List<GitHubIssue> buscarVagas() {
        GitHubIssue[] issues = restClient.get()
                .uri("/repos/backend-br/vagas/issues?state=open&per_page=5")
                .retrieve()
                .body(GitHubIssue[].class);

        return issues == null ? List.of() : List.of(issues);
    }
}
