package br.com.softplan.report.service;

import br.com.softplan.report.exception.BadRequestException;
import br.com.softplan.report.model.NotaFiscal;
import br.com.softplan.report.model.ObservacaoEntity;
import br.com.softplan.report.repository.ObservacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringJoiner;

@Service
public class ObservacaoService {

    @Autowired
    private ObservacaoRepository observacaoRepository;

    // ✅ NOVO: recebe objetos NotaFiscal, gera resposta e SALVA
    public String gerarNotas(List<NotaFiscal> notas) {

        validarNotasFiscais(notas);

        ObservacaoEntity observacao = new ObservacaoEntity();

        // Guarda como string para registro
        // Ex: "[{numero:123, valor:99.9}]"
        String notasSerializadas = serializarNotasFiscais(notas);

        String resposta = tratarPrefixoMensagemNota(notas)
                + tratarSufixoMensagemNota(notas)
                + ".";

        observacao.setNotas(notasSerializadas);
        observacao.setResposta(resposta);

        observacaoRepository.save(observacao);

        return resposta;
    }

    public List<ObservacaoEntity> findAll() {
        return observacaoRepository.findAll();
    }

    // =========================
    // Validação
    // =========================
    private void validarNotasFiscais(List<NotaFiscal> notas) {
        if (notas == null || notas.isEmpty()) {
            throw new BadRequestException("A lista 'notas' não pode ser vazia.");
        }

        for (int i = 0; i < notas.size(); i++) {
            NotaFiscal nf = notas.get(i);

            if (nf == null) {
                throw new BadRequestException("A nota na posição " + i + " está nula.");
            }
            if (nf.getNumero() == null || nf.getNumero() <= 0) {
                throw new BadRequestException("A nota na posição " + i + " precisa ter 'numero' válido (> 0).");
            }
            if (nf.getValor() == null) {
                throw new BadRequestException("A nota na posição " + i + " precisa ter 'valor'.");
            }
            if (nf.getValor().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("O 'valor' da nota na posição " + i + " não pode ser negativo.");
            }
        }
    }

    private String serializarNotasFiscais(List<NotaFiscal> notas) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (NotaFiscal nf : notas) {
            joiner.add("{numero:" + nf.getNumero() + ", valor:" + nf.getValor() + "}");
        }
        return joiner.toString();
    }

    // =========================
    // Mensagens
    // =========================
    private String tratarPrefixoMensagemNota(List<NotaFiscal> notas) {
        if (notas.size() >= 2) {
            return "Fatura das notas fiscais de simples remessa: ";
        }
        return "Fatura da nota fiscal de simples remessa: ";
    }

    private String tratarSufixoMensagemNota(List<NotaFiscal> notas) {

        if (notas.size() == 1) {
            NotaFiscal nf = notas.get(0);
            return "Nota: " + nf.getNumero() + " R$ " + nf.getValor();
        }

        StringJoiner joiner = new StringJoiner(", ");

        for (int i = 0; i < notas.size() - 1; i++) {
            NotaFiscal nf = notas.get(i);
            joiner.add("Nota: " + nf.getNumero() + " R$ " + nf.getValor());
        }

        NotaFiscal ultimo = notas.get(notas.size() - 1);
        return joiner.toString() + " e " + "Nota: " + ultimo.getNumero() + " R$ " + ultimo.getValor();
    }
}
