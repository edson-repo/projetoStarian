package br.com.softplan.report.controller;

import br.com.softplan.report.dto.ObservacaoRequest;
import br.com.softplan.report.model.ObservacaoEntity;
import br.com.softplan.report.service.ObservacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observacoes")
public class ObservacaoController {

    // O controller só orquestra a chamada: valida o caminho e delega a regra de negócio pro service.
    @Autowired
    private ObservacaoService observacaoService;

    @PostMapping("/gerar")
    public String gerar(@RequestBody ObservacaoRequest request) {
        // Recebe as notas no corpo da requisição e devolve a observação gerada.
        return observacaoService.gerarObservacao(request.getNotas());
    }

    @GetMapping("/getAll")
    public List<ObservacaoEntity> findAll() {
        // Lista todas as observações já registradas.
        return observacaoService.findAll();
    }
}
