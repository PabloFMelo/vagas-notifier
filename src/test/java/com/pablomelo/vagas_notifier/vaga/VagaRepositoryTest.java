package com.pablomelo.vagas_notifier.vaga;

import com.pablomelo.vagas_notifier.TestcontainersConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class VagaRepositoryTest {

    @Autowired
    private VagaRepository vagaRepository;

    @AfterEach
    void limparBanco() {
        vagaRepository.deleteAll();
    }

    @Test
    void encontraVagaSalvaPeloGithubId() {
        vagaRepository.saveAndFlush(vaga(123L));

        assertThat(vagaRepository.existsByGithubId(123L)).isTrue();
        assertThat(vagaRepository.existsByGithubId(999L)).isFalse();
    }

    @Test
    void naoPermiteSalvarAMesmaVagaDuasVezes() {
        vagaRepository.saveAndFlush(vaga(123L));

        assertThatThrownBy(() -> vagaRepository.saveAndFlush(vaga(123L)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Vaga vaga(Long githubId) {
        return new Vaga(githubId, "Desenvolvedor Java", "https://github.com/vagas/" + githubId, OffsetDateTime.now());
    }
}
