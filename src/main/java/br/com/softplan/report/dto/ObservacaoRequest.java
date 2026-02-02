package br.com.softplan.report.dto;

import br.com.softplan.report.model.NotaFiscal;

import java.util.List;

public class ObservacaoRequest {

    private List<NotaFiscal> notas;

    public ObservacaoRequest() {
    }

    public ObservacaoRequest(List<NotaFiscal> notas) {
        this.notas = notas;
    }

    public List<NotaFiscal> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaFiscal> notas) {
        this.notas = notas;
    }
}
