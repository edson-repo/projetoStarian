package br.com.softplan.report.application;

import br.com.softplan.report.controller.ObservacaoController;
import br.com.softplan.report.exception.BadRequestException;
import br.com.softplan.report.model.ObservacaoEntity;
import br.com.softplan.report.repository.ObservacaoRepository;
import br.com.softplan.report.service.ObservacaoService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeradorObservacaoTest {

    private GeradorObservacao geradorObservacao;

    private List<List<Integer>> listaDeNumerosInteiros;
    private List<ObservacaoEntity> listaDeObservacao;

    // ⚠️ sem SpringRunner, @Autowired não funciona no JUnit 4.4
    private ObservacaoService observacaoService;
    private ObservacaoRepository observacaoRepository;
    private ObservacaoController observacaoController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {

        geradorObservacao = new GeradorObservacao();

        // 1) "Banco" simulado
        observacaoRepository = mock(ObservacaoRepository.class);

        // 2) Service real (passa por ele) + injeta repository mock
        observacaoService = new ObservacaoService();
        ReflectionTestUtils.setField(observacaoService, "observacaoRepository", observacaoRepository);

        // 3) Controller real + injeta service real
        observacaoController = new ObservacaoController();
        ReflectionTestUtils.setField(observacaoController, "observacaoService", observacaoService);

        // 4) MockMvc sem subir Spring
        mockMvc = MockMvcBuilders.standaloneSetup(observacaoController).build();

        //Carregar lista de numeros
        criarListaDeNumerosInteiros();

        // Carregar lista de observação (AGORA cria a lista)
        criarListaDeObservacao();

        // quando o service chamar repository.findAll(), devolve a lista simulada
        when(observacaoRepository.findAll()).thenReturn(listaDeObservacao);
    }

    //Testes unitarios
    @Test
    public void deve_lancar_excecao_com_mensagem_quando_lista_vazia() {

            String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(0));
            assertEquals("A lista 'notas' não pode ser vazia: 0.", resposta);
    }

    @Test
    public void deve_gerar_observacao_com_uma_nota_refatorado() {
        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(1), false);
        assertEquals("Fatura da nota fiscal de simples remessa: 1.", resposta);
    }

    @Test
    public void deve_gerar_observacao_com_duas_notas_refatorado() {
        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(2), false);
        assertEquals("Fatura das notas fiscais de simples remessa: 1 e 2.", resposta);
    }

    @Test
    public void deve_gerar_observacao_com_tres_notas_refatorado() {
        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(3), false);
        assertEquals("Fatura das notas fiscais de simples remessa: 1, 2 e 3.", resposta);
    }

    //teste "integrado" (Controller -> Service -> Repository mock)
    @Test
    public void teste_integracao_get_all_controller() throws Exception {

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/observacoes/getAll"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.length()").value(3));
    }

    //LEGADOS
    @Test
    public void deve_gerar_observacao_sem_nota() {
        List<Integer> numerosNotaFiscal = new ArrayList<>();
        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
        assertEquals("", observacao);
    }

    @Test
    public void deve_gerar_observacao_com_uma_nota() {
        List<Integer> numerosNotaFiscal = singletonList(1);
        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
        assertEquals("Fatura da nota fiscal de simples remessa: 1.", observacao);
    }

    @Test
    public void deve_gerar_observacao_com_duas_notas() {
        List<Integer> numerosNotaFiscal = asList(1, 3);
        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
        assertEquals("Fatura das notas fiscais de simples remessa: 1 e 3.", observacao);
    }

    @Test
    public void deve_gerar_observacao_com_diversas_notas() {
        List<Integer> numerosNotaFiscal = asList(1, 2, 3, 4, 5);
        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
        assertEquals("Fatura das notas fiscais de simples remessa: 1, 2, 3, 4 e 5.", observacao);
    }

    private void criarListaDeNumerosInteiros() {
        listaDeNumerosInteiros = new ArrayList<>(Arrays.asList(
                new ArrayList<>(),
                new ArrayList<>(Arrays.asList(1)),
                new ArrayList<>(Arrays.asList(1, 2)),
                new ArrayList<>(Arrays.asList(1, 2, 3))
        ));
    }

    private void criarListaDeObservacao() {

        ObservacaoEntity e1 = new ObservacaoEntity();
        e1.setNotas(listaDeNumerosInteiros.get(1).toString());
        e1.setResposta("Fatura da nota fiscal de simples remessa: 1.");

        ObservacaoEntity e2 = new ObservacaoEntity();
        e2.setNotas(listaDeNumerosInteiros.get(2).toString());
        e2.setResposta("Fatura das notas fiscais de simples remessa: 1 e 2.");

        ObservacaoEntity e3 = new ObservacaoEntity();
        e3.setNotas(listaDeNumerosInteiros.get(3).toString());
        e3.setResposta("Fatura das notas fiscais de simples remessa: 1, 2 e 3.");

        // ✅ agora a lista existe de verdade
        listaDeObservacao = Arrays.asList(e1, e2, e3);
    }
}

//package br.com.softplan.report.application;
//
//import br.com.softplan.report.controller.ObservacaoController;
//import br.com.softplan.report.exception.BadRequestException;
//import br.com.softplan.report.model.ObservacaoEntity;
//import br.com.softplan.report.repository.ObservacaoRepository;
//import br.com.softplan.report.service.ObservacaoService;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static java.util.Arrays.asList;
//import static java.util.Collections.singletonList;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//import static org.mockito.Mockito.when;
//
//
//public class GeradorObservacaoTest {
//
//    private GeradorObservacao geradorObservacao;
//
//    private List<List<Integer>> listaDeNumerosInteiros;
//    private List<ObservacaoEntity> listaDeObservacao;
//
//    @Autowired
//    private ObservacaoService observacaoService;
//
//    @Autowired
//    private ObservacaoRepository observacaoRepository;
//
//    @Autowired
//    private ObservacaoController observacaoController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//
//        geradorObservacao = new GeradorObservacao();
//        observacaoService = new ObservacaoService();
//
//        //Carregar lista de numeros
//        criarListaDeNumerosInteiros();
//
//        // Carregar lista de observação
//        criarListaDeObservacao();
//
//        //when(observacaoService.findAll()).thenReturn(listaDeObservacao);
//        when(observacaoRepository.findAll()).thenReturn(listaDeObservacao);
//    }
//
//
//    //Testes unitarios
//    @Test
//    public void deve_lancar_excecao_com_mensagem_quando_lista_vazia() {
//        try {
//            observacaoService.gerarObservacao(listaDeNumerosInteiros.get(0));
//            fail("Esperava BadRequestException quando a lista é vazia.");
//        } catch (BadRequestException e) {
//            assertEquals("A lista 'notas' não pode ser vazia.", e.getMessage());
//        }
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_uma_nota_refatorado() {
//
//        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(1), false);
//        assertEquals("Fatura da nota fiscal de simples remessa: 1.", resposta);
//
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_duas_notas_refatorado() {
//
//        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(2), false);
//        assertEquals("Fatura das notas fiscais de simples remessa: 1 e 2.", resposta);
//
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_tres_notas_refatorado() {
//
//        String resposta = observacaoService.gerarObservacao(listaDeNumerosInteiros.get(3), false);
//        assertEquals("Fatura das notas fiscais de simples remessa: 1, 2 e 3.", resposta);
//
//    }
//
//    //teste Integrado envolvendo controller e service
//    @Test
//    public void teste_integracao_get_all_controller() throws Exception {
//
//        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/observacoes/getAll"))
//                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
//                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
//                        .contentTypeCompatibleWith(org.springframework.http.MediaType.APPLICATION_JSON))
//                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.length()").value(3));
//    }
//
//
//    //LEGADOS
//    @Test
//    public void deve_gerar_observacao_sem_nota() {
//        List<Integer> numerosNotaFiscal = new ArrayList<>();
//
//        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
//
//        assertEquals("", observacao);
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_uma_nota() {
//        List<Integer> numerosNotaFiscal = singletonList(1);
//
//        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
//
//        assertEquals("Fatura da nota fiscal de simples remessa: 1.", observacao);
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_duas_notas() {
//        List<Integer> numerosNotaFiscal = asList(1, 3);
//
//        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
//
//        assertEquals("Fatura das notas fiscais de simples remessa: 1 e 3.", observacao);
//    }
//
//    @Test
//    public void deve_gerar_observacao_com_diversas_notas() {
//        List<Integer> numerosNotaFiscal = asList(1, 2, 3, 4, 5);
//
//        String observacao = geradorObservacao.geraObservacao(numerosNotaFiscal);
//
//        assertEquals("Fatura das notas fiscais de simples remessa: 1, 2, 3, 4 e 5.", observacao);
//    }
//
//    private void criarListaDeNumerosInteiros() {
//
//        listaDeNumerosInteiros = new ArrayList<>(Arrays.asList(
//                new ArrayList<>(),                       // vazia (mutável)
//                new ArrayList<>(Arrays.asList(1)),       // [1] (mutável)
//                new ArrayList<>(Arrays.asList(1, 2)),    // [1,2] (mutável)
//                new ArrayList<>(Arrays.asList(1, 2, 3))  // [1,2,3] (mutável)
//        ));
//    }
//
//    private void criarListaDeObservacao() {
//
//        ObservacaoEntity observacaoEntity = new ObservacaoEntity();
//
//        observacaoEntity = new ObservacaoEntity();
//        observacaoEntity.setNotas(listaDeNumerosInteiros.get(1).toString());
//        observacaoEntity.setResposta("Fatura da nota fiscal de simples remessa: 1.");
//        //observacaoRepository.save(observacaoEntity);
//
//        observacaoEntity = new ObservacaoEntity();
//        observacaoEntity.setNotas(listaDeNumerosInteiros.get(2).toString());
//        observacaoEntity.setResposta("Fatura da nota fiscal de simples remessa: 1.");
//        //observacaoRepository.save(observacaoEntity);
//
//        observacaoEntity = new ObservacaoEntity();
//        observacaoEntity.setNotas(listaDeNumerosInteiros.get(3).toString());
//        observacaoEntity.setResposta("Fatura da nota fiscal de simples remessa: 1.");
//        //observacaoRepository.save(observacaoEntity);
//
//    }
//
//}
