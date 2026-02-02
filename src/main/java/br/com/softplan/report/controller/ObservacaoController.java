package br.com.softplan.report.controller;

import br.com.softplan.report.dto.ObservacaoResponse;
import br.com.softplan.report.mapper.ObservacaoMapper;
import br.com.softplan.report.model.NotaFiscal;
import br.com.softplan.report.model.ObservacaoEntity;
import br.com.softplan.report.service.ObservacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/observacoes")
public class ObservacaoController {

    @Autowired
    private ObservacaoService observacaoService;

    // ✅ Mantido como você pediu: List<NotaFiscal> direto no body
    @PostMapping("/gerarNotas")
    public String gerarNotas(@RequestBody List<NotaFiscal> notas) {
        return observacaoService.gerarNotas(notas);
    }

    @GetMapping("/getAll")
    public List<ObservacaoResponse> findAll() {
        List<ObservacaoEntity> observacoes = observacaoService.findAll();

        return observacoes.stream()
                .map(ObservacaoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
