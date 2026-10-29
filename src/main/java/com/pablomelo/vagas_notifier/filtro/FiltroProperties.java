package com.pablomelo.vagas_notifier.filtro;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "vagas.filtro")
public record FiltroProperties(
        List<String> palavrasChave,
        List<String> labelsExcluidas
) {
    public FiltroProperties {
        palavrasChave = palavrasChave == null ? List.of() : palavrasChave;
        labelsExcluidas = labelsExcluidas == null ? List.of() : labelsExcluidas;
    }
}
