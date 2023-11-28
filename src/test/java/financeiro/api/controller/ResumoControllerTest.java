package financeiro.api.controller;

import financeiro.api.service.ResumoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ResumoControllerTest {

    @MockBean
    private ResumoService resumoService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deveria retornar codigo 200 para requisição do tipo GET para a url /resumo/{ano}/{mes}/")
    void cenario01()throws Exception{
        int ano = 2023;
        int mes = 11;

        MockHttpServletResponse response = mockMvc.perform(
                get("/resumo/{ano}/{mes}/",ano,mes)
        ).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

}