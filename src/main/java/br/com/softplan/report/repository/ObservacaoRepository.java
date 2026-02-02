package br.com.softplan.report.repository;

import br.com.softplan.report.model.ObservacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacaoRepository extends JpaRepository<ObservacaoEntity, Long> {
    // Spring Data já entrega save, findAll, etc.
}
