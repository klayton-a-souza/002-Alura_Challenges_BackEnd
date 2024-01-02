package financeiro.api.service;

import financeiro.api.dto.receita.AtualizacaoParcialReceitaDto;
import financeiro.api.dto.receita.AtualizacaoTotalReceitaDto;
import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.dto.receita.ReceitaMesDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.receita.Receita;
import financeiro.api.repository.ReceitaRepository;
import org.hamcrest.collection.IsEmptyCollection;
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
    @DisplayName("Testando o metodo listar quando uma descrição e enviada como parametro")
    void listar01() {
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

    @Test
    @DisplayName("Testando o metodo listar quando uma descrição não e enviada como parametro")
    void listar02() {
        Pageable paginacao = PageRequest.of(0,5);

        Receita receita01 = new Receita(
                2L,
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.now(),
                true
        );
        Receita receita02 = new Receita(
                3L,
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.of(2023,10,07,10,0),
                true
        );

        List<Receita> resultadoFindAllByAtivoTrue = Arrays.asList(receita01,receita02);
        when(receitaRepository.findAllByAtivoTrue(paginacao)).thenReturn(new PageImpl<>(resultadoFindAllByAtivoTrue));

        List<ReceitaDto> resultadoListar = receitaService.listar(paginacao,null);


        verify(receitaRepository, times(1)).findAllByAtivoTrue(paginacao);
        assertNotNull(resultadoListar);
        assertEquals(resultadoFindAllByAtivoTrue.size(), resultadoListar.size());

    }
    @Test
    @DisplayName("Testando detalhamento de uma receita passando um ID que esta cadastrado no banco de dados!")
    void detalhar01(){
        Long id_receita = 1L;
        Receita receita = new Receita(1L,"Teste POST",new BigDecimal(600.00), LocalDateTime.now(),true);
        when(receitaRepository.existsById(id_receita)).thenReturn(true);
        when(receitaRepository.getReferenceById(id_receita)).thenReturn(receita);

        receitaService.detalhar(id_receita);


        verify(receitaRepository,times(1)).getReferenceById(id_receita);
    }
    @Test
    @DisplayName("Testando detalhamento de uma receita passando um ID que não esta cadastrado no banco de dados!")
    void detalhar02(){

        Long id_receita = 1L;
        Receita receita = new Receita(1L,"Teste POST",new BigDecimal(600.00), LocalDateTime.now(),true);
        when(receitaRepository.existsById(id_receita)).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> receitaService.detalhar(id_receita));

       verify(receitaRepository,times(1)).existsById(id_receita);
    }

    @Test
    @DisplayName("Testando se a atualização parcial da receita esta alterando os valor (Descrição) corretamente")
    void atualizacaoParcial01(){
        AtualizacaoParcialReceitaDto dto = new AtualizacaoParcialReceitaDto(
                1L,
                "parcial",
                null,
                null);
        Receita receita = new Receita(
                1L,
                "teste",
                new BigDecimal(1000.00)
                ,LocalDateTime.of(2024,Month.JANUARY,1,10,10,0)
                ,true);

        when(receitaRepository.existsById(dto.id_receita())).thenReturn(true);
        when(receitaRepository.getReferenceById(dto.id_receita())).thenReturn(receita);

        receitaService.parcial(dto);

        assertEquals(receita.getDescricao(),dto.descricao());
        assertNotEquals(receita.getValor(),dto.valor());
    }
    @Test
    @DisplayName("Testando se a atualização parcial da receita esta alterando os valor (Descrição) corretamente")
    void atualizaçãoTotal(){
        AtualizacaoTotalReceitaDto dto = new AtualizacaoTotalReceitaDto(
                1L,
                "total",
                new BigDecimal(700.00),
                LocalDateTime.now());
        Receita receita = new Receita(
                1L,
                "parcial",
                new BigDecimal(1000.00)
                ,LocalDateTime.of(2024,Month.JANUARY,1,10,10,0)
                ,true);

        when(receitaRepository.existsById(dto.id_receita())).thenReturn(true);
        when(receitaRepository.getReferenceById(dto.id_receita())).thenReturn(receita);

        receitaService.total(dto);

        assertEquals(receita.getDescricao(),dto.descricao());
        assertEquals(receita.getValor(),dto.valor());
        assertEquals(receita.getData(),dto.data());

    }

    @Test
    @DisplayName("Verificando se a exclução logica foi executado corretamente, o parametro (ativo) deve ser alterado")
    void deletar(){
        Long id_receita = 1L;
        Receita receita = new Receita(
                1L,
                "parcial",
                new BigDecimal(1000.00)
                ,LocalDateTime.of(2024,Month.JANUARY,1,10,10,0)
                ,true);
        when(receitaRepository.existsById(id_receita)).thenReturn(true);
        when(receitaRepository.getReferenceById(id_receita)).thenReturn(receita);

        receitaService.exclusaoLogica(id_receita);
        assertEquals(receita.getAtivo(),false);
    }

    @Test
    @DisplayName("Executando um select na tabela de receitas com mes e ano especifico!")
    void listarMes01(){
        int ano = 2024;
        int mes = 1;

        ReceitaMesDto receita01 = new ReceitaMesDto(
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.now()
        );
        ReceitaMesDto receita02 = new ReceitaMesDto(
                "Potter",
                new BigDecimal(50.00),
                LocalDateTime.of(2024,1,1,10,0)
        );
        List<ReceitaMesDto> receitaDtoList = Arrays.asList(receita01,receita02);
        when(receitaRepository.listarPorMesEAno(ano,mes)).thenReturn(receitaDtoList);

        receitaService.listarMes(ano,mes);

        verify(receitaRepository,times(1)).listarPorMesEAno(ano,mes);
        assertEquals(receitaDtoList.size(),2);
    }
    @Test
    @DisplayName("Retornar uma exception pelo fato de que na data especifica não foi encontrado nenhum registro")
    void listarMes02(){
        int ano = 2024;
        int mes = 1;
        when(receitaRepository.listarPorMesEAno(ano,mes)).thenReturn(new ArrayList<>());

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> receitaService.listarMes(ano, mes));

        assertEquals("Não foi possivel encontrar nenhuma receita no banco de dados nessa data!",exception.getMessage());
        verify(receitaRepository, times(1)).listarPorMesEAno(ano, mes);
    }



}