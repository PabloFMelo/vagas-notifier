package com.pablomelo.vagas_notifier.filtro;

import com.pablomelo.vagas_notifier.github.GitHubIssue;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FiltroVagasTest {

    @Test
    void aceitaVagaComPalavraChaveNoTitulo() {
        FiltroVagas filtro = filtro(List.of("java"), List.of());

        assertThat(filtro.aceita(vaga("Desenvolvedor Java Pleno"))).isTrue();
    }

    @Test
    void ignoraDiferencaEntreMaiusculasEMinusculas() {
        FiltroVagas filtro = filtro(List.of("java"), List.of());

        assertThat(filtro.aceita(vaga("DESENVOLVEDOR JAVA"))).isTrue();
    }

    @Test
    void aceitaVagaComPalavraChaveNasLabels() {
        FiltroVagas filtro = filtro(List.of("java"), List.of());

        assertThat(filtro.aceita(vaga("Engenheiro de Software Backend", "Java", "Sênior"))).isTrue();
    }

    @Test
    void rejeitaVagaSemPalavraChave() {
        FiltroVagas filtro = filtro(List.of("java"), List.of());

        assertThat(filtro.aceita(vaga("Desenvolvedor Python"))).isFalse();
    }

    @Test
    void rejeitaVagaComLabelExcluida() {
        FiltroVagas filtro = filtro(List.of("java"), List.of("Presencial"));

        assertThat(filtro.aceita(vaga("Desenvolvedor Java", "Presencial"))).isFalse();
    }


    @Test
    void aceitaQualquerVagaQuandoNaoHaPalavrasChave() {
        FiltroVagas filtro = filtro(List.of(), List.of());

        assertThat(filtro.aceita(vaga("Desenvolvedor Python"))).isTrue();
    }

    @Test
    void naoConfundeJavaComJavascript() {
        FiltroVagas filtro = filtro(List.of("java"), List.of());

        assertThat(filtro.aceita(vaga("Desenvolvedor JavaScript"))).isTrue();

    }

    private FiltroVagas filtro(List<String> palavrasChave, List<String> labelsExcluidas) {
        return new FiltroVagas(new FiltroProperties(palavrasChave, labelsExcluidas));
    }

    private GitHubIssue vaga(String titulo, String... labels) {
        List<GitHubIssue.Label> listaDeLabels = Arrays.stream(labels)
                .map(GitHubIssue.Label::new)
                .toList();

        return new GitHubIssue(1L, titulo, "https://exemplo.com", OffsetDateTime.now(), listaDeLabels);
    }
}
