package com.pablomelo.vagas_notifier.vaga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    boolean existsByGithubId(Long githubId);
}
