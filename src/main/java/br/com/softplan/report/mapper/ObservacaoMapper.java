package br.com.softplan.report.mapper;

import br.com.softplan.report.dto.ObservacaoResponse;
import br.com.softplan.report.model.ObservacaoEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ObservacaoMapper {

    private ObservacaoMapper() {
        // utilitário: não precisa instanciar
    }

    public static ObservacaoResponse toResponse(ObservacaoEntity e) {
        return new ObservacaoResponse(
                e.getId(),
                parseNotas(e.getNotas()),
                e.getResposta()
        );
    }

    private static List<Integer> parseNotas(String notas) {
        String conteudo = extrairConteudoDasNotas(notas);
        if (conteudo.isEmpty()) return Collections.emptyList();

        return Arrays.stream(conteudo.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static String extrairConteudoDasNotas(String notas) {
        if (notas == null) return "";
        return notas.replace("[", "")
                .replace("]", "")
                .trim();
    }
}
