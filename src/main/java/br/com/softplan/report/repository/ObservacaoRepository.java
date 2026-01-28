package br.com.softplan.report.repository;

import br.com.softplan.report.model.ObservacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacaoRepository extends JpaRepository<ObservacaoEntity, Long> {
    // De propósito fica vazio: o Spring Data já entrega os métodos básicos (save, findAll, etc.).
}
