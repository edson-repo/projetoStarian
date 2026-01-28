package br.com.softplan.report.dto;

import java.util.List;

public class ObservacaoResponse {

    // Identificador do registro (quando a observação é persistida).
    private Long id;

    // Entrada usada pra gerar a observação (a lista de notas).
    private List<Integer> entrada;

    // Texto final gerado a partir da entrada.
    private String saida;

    public ObservacaoResponse(Long id, List<Integer> entrada, String saida) {
        // DTO direto ao ponto: só empacota os dados de retorno.
        this.id = id;
        this.entrada = entrada;
        this.saida = saida;
    }

    public Long getId() {
        return id;
    }

    public List<Integer> getEntrada() {
        return entrada;
    }

    public String getSaida() {
        return saida;
    }
}
