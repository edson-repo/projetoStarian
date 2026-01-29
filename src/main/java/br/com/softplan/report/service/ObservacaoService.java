package br.com.softplan.report.service;

import br.com.softplan.report.exception.BadRequestException;
import br.com.softplan.report.model.ObservacaoEntity;
import br.com.softplan.report.repository.ObservacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

@Service
public class ObservacaoService {

    @Autowired
    private ObservacaoRepository observacaoRepository;

    public String gerarObservacao(List<Integer> listaDeNumerosInteiros) {

        ObservacaoEntity observacao = new ObservacaoEntity();

        // Guardamos as notas como string (ex.: "[1, 2, 3]") só pra registrar.
        String notas = listaDeNumerosInteiros.toString();
        String resposta = tratarPrefixoMensagem(listaDeNumerosInteiros) + tratarSufixoMensagem(listaDeNumerosInteiros) + ".";

        observacao.setNotas(notas);
        observacao.setResposta(resposta);

        observacaoRepository.save(observacao);

        return resposta;
    }

    public String gerarObservacao(List<Integer> listaDeNumerosInteiros, boolean salvar) {

        // Mantém a mesma validação do método principal.
        if (listaDeNumerosInteiros == null || listaDeNumerosInteiros.isEmpty())
            throw new BadRequestException("A lista 'notas' não pode ser vazia.");

        ObservacaoEntity observacao = new ObservacaoEntity();

        String notas = listaDeNumerosInteiros.toString();
        String resposta = tratarPrefixoMensagem(listaDeNumerosInteiros) + tratarSufixoMensagem(listaDeNumerosInteiros) + ".";

        observacao.setNotas(notas);
        observacao.setResposta(resposta);

        // Útil pra testes: gera a mensagem sem necessariamente gravar no banco.
        if (salvar)
            observacaoRepository.save(observacao);

        return resposta;
    }

    public List<ObservacaoEntity> findAll() {
        // Sem regra extra aqui: só repassa a consulta.
        return observacaoRepository.findAll();
    }

    private String tratarPrefixoMensagem(List<Integer> listaDeNumerosInteiros) {

        // Regra básica: sem notas, não tem como gerar observação.
        if (listaDeNumerosInteiros == null || listaDeNumerosInteiros.isEmpty())
            return "A lista 'notas' não pode ser vazia: ";

        // Pluraliza quando tem mais de uma nota.
        if (listaDeNumerosInteiros.size() >= 2)
            return "Fatura das notas fiscais de simples remessa: ";

        // A lista esta zerada
        if (listaDeNumerosInteiros.get(0)==0 && listaDeNumerosInteiros.size()==1)
            return "A lista 'notas' não contém nehuma fatura: ";

        return "Fatura da nota fiscal de simples remessa: ";

    }

    private String tratarSufixoMensagem(List<Integer> listaDeNumerosInteiros) {

        if (listaDeNumerosInteiros == null ||
                listaDeNumerosInteiros.isEmpty() ||
                (listaDeNumerosInteiros.get(0)==0 && listaDeNumerosInteiros.size()==1))
            return "0";

        // Caso simples: só uma nota.
        if (listaDeNumerosInteiros.size() == 1)
            return listaDeNumerosInteiros.get(listaDeNumerosInteiros.size() - 1).toString();

        // Monta a lista com vírgula e deixa o último com " e ".
        StringJoiner joiner = new StringJoiner(", ");

        // Do primeiro até o penúltimo vai com vírgula.
        for (int i = 0; i < listaDeNumerosInteiros.size() - 1; i++) {
            joiner.add(String.valueOf(listaDeNumerosInteiros.get(i)));
        }

        // O último entra separado por " e ".
        int ultimo = listaDeNumerosInteiros.get(listaDeNumerosInteiros.size() - 1);
        return joiner.toString() + " e " + ultimo;
    }
}
