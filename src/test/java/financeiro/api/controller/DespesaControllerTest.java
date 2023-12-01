package financeiro.api.controller;

import financeiro.api.dto.despesa.AtualizacaoPacialDespesaDto;
import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.repository.DespesaRepository;
import financeiro.api.service.DespesaService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DespesaControllerTest {

    @MockBean
    private DespesaService despesaService;
    @Autowired
    MockMvc mockMvc;
    @Mock
    private DespesaRepository repository;
    @Captor
    private ArgumentCaptor<Despesa> despesaCaptor;

    @Test
    @DisplayName("Deveria retornar 201 para requisições POST para o endereço /despesas")
    void cenarioPost01() throws Exception{

        String json =
        """
        {
            "descricao":"League of Legends - Skin",
            "valor":20.50,
            "data":"2023-10-15T09:00:00",
            "categoria":"Lazer"
        }
        """;
        mockMvc.perform(
                post("/despesas")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deveria retornar 400 para requisições POST para o endereço /despesas, com parametro errado")
    void cenarioPost02() throws Exception{

        String json =
                """
                {
                    "descricao":"League of Legends - Skin",
                    "valor":20.50,
                    "data":"2023-10-15T09:00:00",
                    "categoria":"LoL"
                }
                """;
        mockMvc.perform(
                post("/despesas")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Deveria retornar 200 para requisições GET que tentam listar todas as despesas sem mandar a descrição como parametro")
    void listarGet01() throws Exception{
        mockMvc.perform(
                get("/despesas")).andExpect(status().isOk());
    }
    @Test
    @DisplayName("Deveria retornar 200 para requisições GET que tentam listar todas as despesas filtrando pela descricao")
    void listarGet02() throws Exception{
        String descricao = "Salario";

        MockHttpServletResponse response = mockMvc.perform(get("/despesas?descricao={descricao}/",descricao)).andReturn().getResponse();
        assertEquals(200,response.getStatus());
    }
    @Test
    @DisplayName("Deveria retornar 200 para requisições GET que tentam listar todas as despesas filtrando pela data")
    void listarGet03() throws Exception{
        int ano = 2023;
        int mes = 11;

        MockHttpServletResponse response = mockMvc.perform(get("/despesas/{ano}/{mes}/",ano,mes)).andReturn().getResponse();
        assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("Deveria retornar 200 para requisições GET que detalha uma despesa passando o ID")
    void detalharDespesa01() throws Exception{
        Long id_despesa = 1L;
        when(despesaService.detalhar(id_despesa)).thenReturn(new Despesa(1L,"Teste automatizado",new BigDecimal("200.00"),LocalDateTime.now(),true,Categoria.Lazer));

        MockHttpServletResponse response = mockMvc.perform(get("/despesas/{id_despesa}",id_despesa)).andReturn().getResponse();
        assertEquals(200,response.getStatus());
    }
    @Test
    @DisplayName("Deveria retornar 404 para requisições de detalhar de uma despesa que não esta cadastrada no banco de dados!")
    void detalharDespesa02() throws Exception{
        //Arrange
        Long id_despesa = 1L;
        when(despesaService.detalhar(id_despesa)).thenThrow(ValidacaoException.class);
        //Act
        MockHttpServletResponse response = mockMvc.perform(get("/despesas/{id_despesa}",id_despesa)).andReturn().getResponse();
        //Assert
        assertEquals(404,response.getStatus());
    }

    @Test
    void patchDespesa01() throws Exception{

        String json =
                """
                {
                    "id_despesa":1,
                    "descricao":"LoL"
                }
                """;

        AtualizacaoPacialDespesaDto dto = new AtualizacaoPacialDespesaDto(1L,"LoL",null,null,null);
        Despesa despesa = new Despesa(1L,"LoL",new BigDecimal("70.00"), LocalDateTime.now(),true,Categoria.Lazer);
        when(despesaService.parcial(dto)).thenReturn(despesa);

        MockHttpServletResponse response = mockMvc.perform(patch("/despesas")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200,response.getStatus());
        //mockMvc.perform(patch("/despesas").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
