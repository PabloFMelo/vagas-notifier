package com.pablomelo.vagas_notifier.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubIssue(
        Long id,
        String title,
        @JsonProperty("html_url") String url,
        @JsonProperty("created_at") OffsetDateTime criadaEm
) {
}
