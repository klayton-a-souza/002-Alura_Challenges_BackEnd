package financeiro.api.service;

import financeiro.api.dto.despesa.AtualizacaoPacialDespesaDto;
import financeiro.api.dto.despesa.AtualizacaoTotalDespesaDto;
import financeiro.api.dto.despesa.DespesaDataDto;
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
import java.util.ArrayList;
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
    @Test
    @DisplayName("Testando se a atualização parcial da despesa esta alterando os valor (Descrição) corretamente")
    void parcial01(){
        AtualizacaoPacialDespesaDto dto = new AtualizacaoPacialDespesaDto(
                1L,
                "Harry Potter e o Prisioneirode Azkaban",
                null,
                null,
                null);

        Despesa despesa = new Despesa(
                1L,
                "Parcial",
                new BigDecimal(100.00),
                LocalDateTime.now(),
                true,
                Categoria.Lazer);
        when(despesaRepository.existsById(dto.id_despesa())).thenReturn(true);
        when(despesaRepository.getReferenceById(dto.id_despesa())).thenReturn(despesa);

        despesaService.parcial(dto);

        assertEquals(despesa.getDescricao(),despesa.getDescricao());
        assertNotEquals(despesa.getValor(),dto.valor());
    }

    @Test
    @DisplayName("Testando se a atualização total da despesa esta alterando os valor corretamente")
    void total(){
        AtualizacaoTotalDespesaDto dto = new AtualizacaoTotalDespesaDto(
                1L,
                "Harry Potter e o Prisioneirode Azkaban",
                new BigDecimal(78.50),
                LocalDateTime.now(),
                Categoria.Lazer);

        Despesa despesa = new Despesa(
                1L,
                "Parcial",
                new BigDecimal(100.00),
                LocalDateTime.of(2023,Month.DECEMBER,1,10,10,0),
                true,
                Categoria.Outras);
        when(despesaRepository.existsById(dto.id_despesa())).thenReturn(true);
        when(despesaRepository.getReferenceById(dto.id_despesa())).thenReturn(despesa);

        despesaService.total(dto);

        assertEquals(despesa.getDescricao(),dto.descricao());
        assertEquals(despesa.getValor(),dto.valor());
        assertEquals(despesa.getData(),dto.data());
        assertEquals(despesa.getCategoria(),dto.categoria());
    }

    @Test
    @DisplayName("Verificando se a exclução logica foi executado corretamente, o parametro (ativo) deve ser alterado")
    void exclusaoLogica(){
        Long id_despesa = 1L;
        Despesa despesa = new Despesa(
                1L,
                "ExclusaoLogica",
                new BigDecimal(100.00),
                LocalDateTime.now(),
                true,
                Categoria.Outras);

        when(despesaRepository.existsById(id_despesa)).thenReturn(true);
        when(despesaRepository.getReferenceById(id_despesa)).thenReturn(despesa);

        despesaService.exclusaoLogica(id_despesa);

        assertEquals(despesa.getAtivo(),false);
    }

    @Test
    @DisplayName("Executando um select na tabela de receitas com mes e ano especifico!")
    void listarPelaData01() {
        int ano = 2024;
        int mes = 1;

        DespesaDataDto despesa01 = new DespesaDataDto(
                "Harry Potter e o Prisioneiro de Azkaban",
                new BigDecimal(75.80), LocalDateTime.of(2024, Month.JANUARY, 9, 3, 4),
                Categoria.Lazer);
        DespesaDataDto despesa02 = new DespesaDataDto(
                "Harry Potter e a Câmera Secreta",
                new BigDecimal(75.80), LocalDateTime.of(2023, Month.DECEMBER, 9, 3, 4),
                Categoria.Lazer);
        List<DespesaDataDto> listaDespesas = Arrays.asList(despesa01, despesa02);
        when(despesaRepository.listarPelaData(ano, mes)).thenReturn(listaDespesas);

        despesaService.listarPelaData(ano, mes);

        verify(despesaRepository,times(1)).listarPelaData(ano,mes);
    }

    @Test
    @DisplayName("Retornar uma exception pelo fato de que na data especifica não foi encontrado nenhum registro")
    void listarPelaData02(){
        int ano = 2024;
        int mes = 1;
        when(despesaRepository.listarPelaData(ano,mes)).thenReturn(new ArrayList<>());

        ValidacaoException exception =
                assertThrows(ValidacaoException.class, () -> despesaService.listarPelaData(ano,mes));

        assertEquals("Não foi possivel encontrar nenhuma despesa no banco de dados com essa data!",exception.getMessage());
        verify(despesaRepository,times(1)).listarPelaData(ano,mes);
    }
}