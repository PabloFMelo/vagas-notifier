package com.pablomelo.vagas_notifier.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record GitHubIssue(
        Long id,
        String title,
        @JsonProperty("html_url") String url,
        @JsonProperty("created_at") OffsetDateTime criadaEm,
        List<Label> labels
) {
    public record Label(String name) {}
}
