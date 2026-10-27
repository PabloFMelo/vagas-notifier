package com.pablomelo.vagas_notifier.vaga;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "vagas")

public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false, length = 500)
    private String titulo;

    @Column(nullable = false)
    private String url;

    private OffsetDateTime publicadaEm;

    private OffsetDateTime notificadaEm;

    protected Vaga() {
    }

    public Vaga(Long githubId, String titulo, String url, OffsetDateTime publicadaEm) {
        this.githubId = githubId;
        this.titulo = titulo;
        this.url = url;
        this.publicadaEm = publicadaEm;
        this.notificadaEm = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Long getGithubId() { return githubId; }
    public String getTitulo() { return titulo; }
    public String getUrl() { return url; }
    public OffsetDateTime getPublicadaEm() { return publicadaEm; }
    public OffsetDateTime getNotificadaEm() { return notificadaEm; }
}
