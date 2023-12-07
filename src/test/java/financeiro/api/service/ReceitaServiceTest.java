package financeiro.api.service;

import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.receita.Receita;
import financeiro.api.repository.ReceitaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTest {

    @InjectMocks
    private ReceitaService receitaService;
    @Mock
    private ReceitaRepository receitaRepository;
    @Captor
    private ArgumentCaptor<Receita> receitaCaptor;
    @Captor
    private ArgumentCaptor<Page<Receita>> teste;
    @Mock
    private ReceitaDto receitaDto;


    @Test
    @DisplayName("Testa para cadastrar uma receita com sucesso!")
    void cadastrar01(){
        //Arrange
        ReceitaDto dto = new ReceitaDto(1L,"Teste POST",new BigDecimal(600.00), LocalDateTime.now(),true);
        when(receitaRepository.getReferenceByDescricao(dto.descricao())).thenReturn(Optional.empty());
        when(receitaRepository.save(any())).thenReturn(new Receita());

        //Act
        receitaService.cadastrar(dto);

        //Assert
        verify(receitaRepository).getReferenceByDescricao(dto.descricao());
        verify(receitaRepository).save(receitaCaptor.capture());

        Receita receitaCapturada = receitaCaptor.getValue();

        assertEquals(receitaCapturada.getDescricao(),dto.descricao());
        assertEquals(receitaCapturada.getValor(),dto.valor());
        assertEquals(receitaCapturada.getData(),dto.data());
        assertEquals(receitaCapturada.getAtivo(),dto.ativo());
    }

    @Test
    @DisplayName("Testa para cadastrar uma receita que ja esta cadastrada no banco de dados!")
    void cadastrar02(){
        //Arrange
        ReceitaDto dto = new ReceitaDto(
                1L,
                "Teste Exception - POST",
                new BigDecimal("600.00"),
                LocalDateTime.now(),
                true);
        Receita receita = new Receita(dto);

        when(receitaRepository.getReferenceByDescricao(dto.descricao())).thenReturn(Optional.of(receita));

        assertThrows(ValidacaoException.class, () -> receitaService.cadastrar(dto));

        verify(receitaRepository,never()).save(any());
    }
    @Test
    void testListarComDescricaoExistente() {
        // Configurar dados de teste
        String descricao = "Potter";
        Pageable paginacao = PageRequest.of(0,5);

        ReceitaDto receita01 = new ReceitaDto(
                2L,
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.now(),
                true
        );
        ReceitaDto receita02 = new ReceitaDto(
                3L,
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.of(2023,10,07,10,0),
                true
        );

        List<ReceitaDto> receitaDtoList = Arrays.asList(receita01,receita02);
        when(receitaRepository.buscarPelaDescricao(paginacao,descricao)).thenReturn(new PageImpl<>(receitaDtoList));

        List<ReceitaDto> lista = receitaService.listar(paginacao,descricao);

        assertEquals(lista,receitaDtoList);
    }
}