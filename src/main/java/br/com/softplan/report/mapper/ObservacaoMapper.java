package br.com.softplan.report.mapper;

import br.com.softplan.report.dto.ObservacaoResponse;
import br.com.softplan.report.model.ObservacaoEntity;

public class ObservacaoMapper {

    public static ObservacaoResponse toResponse(ObservacaoEntity entity) {
        return new ObservacaoResponse(
                entity.getId(),
                entity.getNotas(),      // entrada
                entity.getResposta(),   // saida
                entity.getDataCriacao() // data
        );
    }
}
