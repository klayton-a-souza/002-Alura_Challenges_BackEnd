package financeiro.api.service;

import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.repository.DespesaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @InjectMocks
    private DespesaService despesaService;
    @Mock
    private DespesaRepository despesaRepository;
    @Captor
    private ArgumentCaptor<Despesa> despesaCaptor;

    @Test
    @DisplayName("Cadastro de uma nova despesa")
    void  cadastrar01(){
        DespesaDto dto = new DespesaDto(
                1L,
                "Pizza",
                new BigDecimal(35.00),
                LocalDateTime.of(2024, Month.JANUARY,1,8,30,0),
                Categoria.Alimentação,
                true);

        when(despesaRepository.getReferenceByDescricao(dto.descricao())).thenReturn(Optional.empty());
        when(despesaRepository.save(any())).thenReturn(new Despesa());

        despesaService.cadastrar(dto);

        verify(despesaRepository).getReferenceByDescricao(dto.descricao());
        verify(despesaRepository).save(despesaCaptor.capture());

        Despesa despesaCapturada = despesaCaptor.getValue();

        assertEquals(despesaCapturada.getDescricao(),dto.descricao());
        assertEquals(despesaCapturada.getValor(),dto.valor());
        assertEquals(despesaCapturada.getData(),dto.data());
        assertEquals(despesaCapturada.getAtivo(),dto.ativo());
    }

    @Test
    @DisplayName("Verificando se uma excption e lançada ao tentar cadastrar um receita com mesma data que uma que ja esta no banco de dados")
    void cadastrar02(){
        DespesaDto dto = new DespesaDto(
                1L,
                "Pizza",
                new BigDecimal(35.00),
                LocalDateTime.of(2024, Month.JANUARY,1,8,30,0),
                Categoria.Alimentação,
                true);
        Despesa despesa = new Despesa(dto);

        when(despesaRepository.getReferenceByDescricao(dto.descricao())).thenReturn(Optional.of(despesa));

        assertThrows(ValidacaoException.class, () -> despesaService.cadastrar(dto));

        verify(despesaRepository,never()).save(any());
    }
    @Test
    @DisplayName("GET passando o paramatro (descrição)")
    void listar01(){
        String descricao = "Harry";
        Pageable paginacao = PageRequest.of(0,5);

        DespesaDto despesa01 = new DespesaDto(
                1L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,12,25,12,0),
                Categoria.Lazer,
                true);
        DespesaDto despesa02 = new DespesaDto(
                2L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,12,24,12,0),
                Categoria.Lazer,
                true);

        List<DespesaDto> despesaDtoList = Arrays.asList(despesa01,despesa02);
        when(despesaRepository.buscarPelaDescricao(paginacao,descricao)).thenReturn(new PageImpl<>(despesaDtoList));

        List<DespesaDto> lista = despesaService.listar(paginacao,descricao);

        assertEquals(lista,despesaDtoList);
        assertEquals(lista.size(),despesaDtoList.size());
    }

    @Test
    @DisplayName("Testando o metodo listar quando uma descrição não e enviada como parametro")
    void listar02(){

        Pageable paginacao = PageRequest.of(0,5);

        Despesa despesa01 = new Despesa(
                1L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,12,25,12,0),
                true,
                Categoria.Lazer);

        Despesa despesa02 = new Despesa(
                2L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,12,24,12,0),
                true,
                Categoria.Lazer);

        List<Despesa> listaAtivos = Arrays.asList(despesa01,despesa02);
        when(despesaRepository.findAllByAtivoTrue(paginacao)).thenReturn(new PageImpl<>(listaAtivos));

        List<DespesaDto> resultadoListar = despesaService.listar(paginacao,null);

        verify(despesaRepository,times(1)).findAllByAtivoTrue(paginacao);
        assertNotNull(resultadoListar);
        assertEquals(resultadoListar.size(),listaAtivos.size());
    }

    @Test
    @DisplayName("Testando detalhamento de uma despesa passando um ID que esta cadastrado no banco de dados!")
    void detalhar01(){
        Long id_despesa = 1L;
        Despesa despesa = new Despesa(
                1L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,12,25,12,0),
                true,
                Categoria.Lazer);
        when(despesaRepository.existsById(id_despesa)).thenReturn(true);
        when(despesaRepository.getReferenceById(id_despesa)).thenReturn(despesa);

        despesaService.detalhar(id_despesa);

        verify(despesaRepository,times(1)).getReferenceById(id_despesa);
    }

    @Test
    @DisplayName("Testando detalhamento de uma despesa passando um ID que NÃO esta cadastrado no banco de dados!")
    void detalhar02() {
        Long id_despesa = 1L;
        Despesa despesa = new Despesa(
                1L,
                "Harry",
                new BigDecimal(100.00),
                LocalDateTime.of(2023, 12, 25, 12, 0),
                true,
                Categoria.Lazer);

        when(despesaRepository.existsById(id_despesa)).thenReturn(false);

        assertThrows(ValidacaoException.class, ()-> despesaService.detalhar(id_despesa));
        verify(despesaRepository,times(1)).existsById(id_despesa);
        verify(despesaRepository,never()).getReferenceById(id_despesa);
    }


}