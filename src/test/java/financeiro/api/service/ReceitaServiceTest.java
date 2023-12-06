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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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




}