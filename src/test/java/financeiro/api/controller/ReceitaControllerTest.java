package financeiro.api.controller;

import financeiro.api.dto.receita.AtualizacaoParcialReceitaDto;
import financeiro.api.dto.receita.AtualizacaoTotalReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.receita.Receita;
import financeiro.api.repository.ReceitaRepository;
import financeiro.api.service.ReceitaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReceitaControllerTest {

    @MockBean
    private ReceitaService receitaService;
    @Autowired
    MockMvc mockMvc;
    @InjectMocks
    private ReceitaController receitaController;
    @Mock
    private ReceitaRepository receitaRepository;


    @Test
    @DisplayName("Deveria retornar 201 para requisições POST para /receitas")
    void cadastrar01() throws Exception{
        //Arrange
        String json =
                """
                {
                    "id_receita": 1,
                    "descricao": "Teste",
                    "valor": "100.00",
                    "data": "2023-10-15T09:00:00"
                }
                """;
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/receitas")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assertion
        assertEquals(201,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 400 para requisições POST para /receitas passando algum parametro errado")
    void cadastrar02() throws Exception{
        //Arrange

        String json =
                """
                {
                    "id_receita": 1,
                    "descricao": "Teste",
                    "valor": "100.00",
                    "data": "2023/11/01"
                }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/receitas")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assertion
        assertEquals(400,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 200 para requisição tentam listar todas as receitas sem passar a descrição")
    void listar01() throws Exception{

        MockHttpServletResponse response = mockMvc.perform(get("/receitas")).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 200 para requisição tentam listar todas as receitas passando uma descrição como parametro")
    void listar02() throws Exception{
        String descricao = "Salario";

        MockHttpServletResponse response = mockMvc.perform(get("/receitas?descricao={descricao}",descricao)).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 200 para requisição para detalhar uma receita passando o ID")
    void detalhar01() throws Exception{
        Long id_receita = 1L;
        when(receitaService.detalhar(id_receita)).thenReturn(new Receita());

        MockHttpServletResponse response = mockMvc.perform(
                get("/receitas/{id_receita}",id_receita)
        ).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 404 para requisição que tentam detalhar uma receita que não esta cadastrada no banco de dados!")
    void detalhar02() throws Exception{
        Long id_receita = 1L;
        doThrow(new ValidacaoException("Receita não encontrada")).when(receitaService).detalhar(id_receita);

        MockHttpServletResponse response = mockMvc.perform(
                get("/receitas/{id_receitas}",id_receita)
        ).andReturn().getResponse();

        assertEquals(404,response.getStatus());
    }

    @Test
    @DisplayName("Codigo 200, quando uma atualização parcial de receita estiver sendo chamada")
    void parcial01() throws Exception{
        String json =
                """
                {
                    "id_receita": 2,
                    "descricao":"Salario - Emprego fixo"
                }
                """;

        when(receitaService.parcial(any())).thenReturn(
                new Receita(
                        2L,
                        "Salario - Emprego fixo",
                        new BigDecimal("1200.00"),
                        LocalDateTime.now(),
                        true));

        mockMvc.perform(patch("/receitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        Mockito.verify(receitaService,times(1)).parcial(any());
    }


    @Test
    @DisplayName("Codigo 404, quando uma atualização parcial de receita estiver sendo chamada")
    void parcial02() throws Exception{
        String json =
                """
                {
                    "id_receita": 2,
                    "descricao":"Salario - Emprego fixo"
                }
                """;

        doThrow(new ValidacaoException("Receita não encontrada")).when(receitaService).parcial(any());

        mockMvc.perform(patch("/receitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isNotFound());

        Mockito.verify(receitaService,times(1)).parcial(any());
    }

    @Test
    @DisplayName("Codigo 400 quando um valor obrigatorio não e enviado: id_receita")
    void parcial03() throws Exception{
        String json =
                """
                {
                    "descricao": "Teste - BadRequest"
                }
                """;
        mockMvc.perform(
                patch("/receitas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());

        verify(receitaService, never()).parcial(any());
    }

    @Test
    @DisplayName("Codigo 200, quando uma atualização parcial de receita estiver sendo chamada")
    void put01() throws Exception{
        String json =
                """
                {
                    "id_receita": 3,
                    "descricao":"Teste - PUT",
                    "valor":600.50,
                    "data":"2023-12-01T20:20:00"
                }
                """;

        when(receitaService.total(any())).thenReturn(new Receita(
                2L,
                "Teste - PUT",
                new BigDecimal("600.50"),
                LocalDateTime.parse("2023-12-01T20:20:00"),
                true));

        mockMvc.perform(put("/receitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(receitaService,times(1)).total(any());
    }

    @Test
    @DisplayName("Codigo 404, quando uma atualização total de uma receita e chamada")
    void put02() throws Exception{
        String json =
                """
                {
                    "id_receita": 3,
                    "descricao":"Teste - PUT",
                    "valor":600.50,
                    "data":"2023-12-01T20:20:00"
                }
                """;
        doThrow(new ValidacaoException("Receita não encontrada")).when(receitaService).total(any());

        mockMvc.perform(put("/receitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isNotFound());

        verify(receitaService,times(1)).total(any());
    }

    @Test
    @DisplayName("Codigo 400 quando um valor obrigatorio não e enviado: descricao")
    void put03() throws Exception{
        String json =
                """
                {
                    "id_receita": 3,
                    "valor":600.50,
                    "data":"2023-12-01T20:20:00"
                }
                """;
        mockMvc.perform(put("/receitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());

        verify(receitaService,never()).total(any());
    }

    @Test
    @DisplayName("Codigo 200 quando uma exclusão logica passando o id_receita e realizada com sucesso")
    void delete01() throws Exception{
        Long id_receita = 2L;
        doNothing().when(receitaService).exclusaoLogica(id_receita);

        mockMvc.perform(delete("/receitas/{id_receita}",id_receita)).andExpect(status().isNoContent());

        verify(receitaService,times(1)).exclusaoLogica(id_receita);
    }

    @Test
    @DisplayName("Codigo 404 quando a receita não e encontrada passando o ID")
    void delete02() throws Exception{
        Long id_receita = 2L;
        doNothing().doThrow(ValidacaoException.class).when(receitaService).exclusaoLogica(id_receita);

        mockMvc.perform(delete("/receitas/{id_receita}",id_receita)).andExpect(status().isNoContent());

        verify(receitaService,times(1)).exclusaoLogica(id_receita);
    }
}