package br.com.softplan.report.dto;

import java.time.LocalDateTime;

public class ObservacaoResponse {

    private Long id;

    // Agora é String porque no banco "notas" é String
    private String entrada;

    // Agora é String porque no banco "resposta" é String
    private String saida;

    // Opcional (mas útil para mostrar no front)
    private LocalDateTime dataCriacao;

    public ObservacaoResponse(Long id, String entrada, String saida, LocalDateTime dataCriacao) {
        this.id = id;
        this.entrada = entrada;
        this.saida = saida;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public String getEntrada() {
        return entrada;
    }

    public String getSaida() {
        return saida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
