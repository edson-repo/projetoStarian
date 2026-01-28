package br.com.softplan.report.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observacao")
public class ObservacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guarda a lista de notas já “serializada” (ex.: "[1, 2, 3]").
    @Column(name = "notas", nullable = false, length = 1000)
    private String notas;

    // Texto final gerado para aquelas notas.
    @Column(name = "resposta", nullable = false, columnDefinition = "TEXT")
    private String resposta;

    // Preenchida automaticamente na criação do registro.
    @Column(name = "dataCriacao", nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        // Evita ter que setar manualmente a data em cada save.
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
