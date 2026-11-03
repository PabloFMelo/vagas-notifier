package com.pablomelo.vagas_notifier.vaga;

import com.pablomelo.vagas_notifier.filtro.FiltroVagas;
import com.pablomelo.vagas_notifier.github.GitHubIssue;
import com.pablomelo.vagas_notifier.github.GitHubService;
import com.pablomelo.vagas_notifier.notificacao.Notificador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private GitHubService gitHubService;

    @Mock
    private Notificador notificador;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private FiltroVagas filtroVagas;

    @InjectMocks
    private VagaService vagaService;

    @Test
    void notificaESalvaVagaNovaAceitaPeloFiltro() {
        GitHubIssue issue = issue(1L, "Desenvolvedor Java");
        when(gitHubService.buscarVagas()).thenReturn(List.of(issue));
        when(vagaRepository.existsByGithubId(1L)).thenReturn(false);
        when(filtroVagas.aceita(issue)).thenReturn(true);

        vagaService.processarNovasVagas();

        ArgumentCaptor<Vaga> captor = ArgumentCaptor.forClass(Vaga.class);
        verify(notificador).notificar(captor.capture());
        Vaga vagaNotificada = captor.getValue();

        assertThat(vagaNotificada.getGithubId()).isEqualTo(1L);
        assertThat(vagaNotificada.getTitulo()).isEqualTo("Desenvolvedor Java");
        verify(vagaRepository).save(vagaNotificada);
    }

    @Test
    void ignoraVagaJaEnviada() {
        GitHubIssue issue = issue(1L, "Desenvolvedor Java");
        when(gitHubService.buscarVagas()).thenReturn(List.of(issue));
        when(vagaRepository.existsByGithubId(1L)).thenReturn(true);

        vagaService.processarNovasVagas();

        verifyNoInteractions(filtroVagas, notificador);
        verify(vagaRepository, never()).save(any());
    }

    @Test
    void ignoraVagaRejeitadaPeloFiltro() {
        GitHubIssue issue = issue(1L, "Desenvolvedor Python");
        when(gitHubService.buscarVagas()).thenReturn(List.of(issue));
        when(vagaRepository.existsByGithubId(1L)).thenReturn(false);
        when(filtroVagas.aceita(issue)).thenReturn(false);

        vagaService.processarNovasVagas();

        verifyNoInteractions(notificador);
        verify(vagaRepository, never()).save(any());
    }

    @Test
    void naoSalvaVagaQuandoNotificacaoFalhaEContinuaComAsDemais() {
        GitHubIssue vagaComFalha = issue(1L, "Desenvolvedor Java");
        GitHubIssue vagaComSucesso = issue(2L, "Desenvolvedor Spring");
        when(gitHubService.buscarVagas()).thenReturn(List.of(vagaComFalha, vagaComSucesso));
        when(vagaRepository.existsByGithubId(anyLong())).thenReturn(false);
        when(filtroVagas.aceita(any())).thenReturn(true);

        doThrow(new RuntimeException("Telegram fora do ar"))
                .doNothing()
                .when(notificador).notificar(any());

        vagaService.processarNovasVagas();

        verify(notificador, times(2)).notificar(any());

        ArgumentCaptor<Vaga> captor = ArgumentCaptor.forClass(Vaga.class);
        verify(vagaRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getGithubId()).isEqualTo(2L);
    }

    private GitHubIssue issue(Long id, String titulo) {
        return new GitHubIssue(id, titulo, "https://github.com/vagas/" + id, OffsetDateTime.now(), List.of());
    }
}
