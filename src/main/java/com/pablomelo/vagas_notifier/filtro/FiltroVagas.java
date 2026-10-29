package com.pablomelo.vagas_notifier.filtro;

import com.pablomelo.vagas_notifier.github.GitHubIssue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FiltroVagas {

    private final FiltroProperties properties;

    public FiltroVagas(FiltroProperties properties) {
        this.properties = properties;
    }

    public boolean aceita(GitHubIssue issue) {
        List<String> labels = issue.labels().stream()
                .map(label -> label.name().toLowerCase())
                .toList();

        boolean temLabelExcluida = properties.labelsExcluidas().stream()
                .anyMatch(excluida -> labels.contains(excluida.toLowerCase()));

        if (temLabelExcluida) {
            return false;
        }

        if (properties.palavrasChave().isEmpty()) {
            return true;
        }

        String texto = (issue.title() + " " + String.join(" ", labels)).toLowerCase();

        return properties.palavrasChave().stream()
                .anyMatch(palavra -> texto.contains(palavra.toLowerCase()));
    }
}
