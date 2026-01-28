package br.com.softplan.report.dto;

import java.util.List;

public class ObservacaoRequest {

    // Lista de números das notas fiscais que o cliente manda na requisição.
    private List<Integer> notas;

    public List<Integer> getNotas() {
        return notas;
    }

    public void setNotas(List<Integer> notas) {
        // Mantém o DTO simples: apenas recebe e expõe os dados.
        this.notas = notas;
    }
}
