package financeiro.api.service;

import financeiro.api.dto.resumo.ResumoDto;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.repository.DespesaRepository;
import financeiro.api.repository.ReceitaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResumoServiceTest {
    @InjectMocks
    private ResumoService resumoService;
    @Mock
    private ReceitaRepository receitaRepository;
    @Mock
    private DespesaRepository despesaRepository;

    @Test
    @DisplayName("Testando se os valores de um mes especifico passando o ano/mes")
    void testResumoMes() {

        int ano = 2023;
        int mes = 1;

        BigDecimal receitaTotalMock = new BigDecimal("1000.00");
        BigDecimal despesaTotalMock = new BigDecimal("500.00");
        BigDecimal saldoMock = new BigDecimal("500.00");

        BigDecimal[] valoresCategoriaMock = {
                new BigDecimal("50.00"), new BigDecimal("100.00"), new BigDecimal("200.00"),
                new BigDecimal("30.00"), new BigDecimal("40.00"), new BigDecimal("10.00"),
                new BigDecimal("25.00"), new BigDecimal("45.00")
        };

        when(receitaRepository.receitaDoMes(ano, mes)).thenReturn(receitaTotalMock);
        when(despesaRepository.despesaDoMes(ano, mes)).thenReturn(despesaTotalMock);

        for (int i = 0; i < Categoria.values().length; i++) {
            when(despesaRepository.totalCategoria(ano, mes, Categoria.values()[i])).thenReturn(valoresCategoriaMock[i]);
        }

        // Chamar o método a ser testado
        ResumoDto resumoDto = resumoService.resumoMes(ano, mes);

        // Verificar se os resultados são os esperados
        assertEquals(receitaTotalMock,resumoDto.receitaMes());
        assertEquals(despesaTotalMock,resumoDto.despesaMes());
        assertEquals(saldoMock,resumoDto.saldo());

        assertEquals(valoresCategoriaMock[0],resumoDto.alimentação());
        assertEquals(valoresCategoriaMock[1],resumoDto.saude());
        assertEquals(valoresCategoriaMock[2],resumoDto.moradia());
        assertEquals(valoresCategoriaMock[3],resumoDto.transporte());
        assertEquals(valoresCategoriaMock[4],resumoDto.educacao());
        assertEquals(valoresCategoriaMock[5],resumoDto.lazer());
        assertEquals(valoresCategoriaMock[6],resumoDto.imprevistos());
        assertEquals(valoresCategoriaMock[7],resumoDto.outras());


    }
}