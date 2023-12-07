package financeiro.api.service;

import financeiro.api.dto.receita.AtualizacaoParcialReceitaDto;
import financeiro.api.dto.receita.AtualizacaoTotalReceitaDto;
import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.dto.receita.ReceitaMesDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.receita.Receita;
import financeiro.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public Receita cadastrar(ReceitaDto dto) {
        verificarReceitaComMesmaDescricaoNoMesmoMes(dto);
        return receitaRepository.save(new Receita(dto));
    }

    private void verificarReceitaComMesmaDescricaoNoMesmoMes(ReceitaDto dto) {
        Optional<Receita> receitaComMesmaDescricao = receitaRepository.getReferenceByDescricao(dto.descricao());
        if(!receitaComMesmaDescricao.isEmpty()){
            if(receitaComMesmaDescricao.get().getData().getMonthValue() == dto.data().getMonthValue()){
                throw new ValidacaoException("A API não deve permitir o cadastro de receitas duplicadas(contendo a mesma descrição, dentro do mesmo mês");
            }
        }
    }

    public List<ReceitaDto> listar(Pageable paginacao,String descricao) {
        if((descricao !=null) && (!descricao.isEmpty())){
            List<ReceitaDto> receitas = receitaRepository.buscarPelaDescricao(paginacao,descricao).stream().toList();
            if(receitas.isEmpty()){
                throw new ValidacaoException("Não foi encontrado nenhuma receitacom essa descrição");
            }
            return receitas;
        }else {
            return receitaRepository.findAllByAtivoTrue(paginacao).stream().map(ReceitaDto::new).toList();
        }
    }

    public Receita detalhar(Long id_receita) {
        validar(id_receita);
        return receitaRepository.getReferenceById(id_receita);
    }

    public Receita parcial(AtualizacaoParcialReceitaDto dto) {
        validar(dto.id_receita());
        Receita receita = receitaRepository.getReferenceById(dto.id_receita());
        receita.parcial(dto);
        return receita;
    }
    public Receita total(AtualizacaoTotalReceitaDto dto) {
        validar(dto.id_receita());
        Receita receita = receitaRepository.getReferenceById(dto.id_receita());
        receita.total(dto);
        return receita;
    }
    public void exclusaoLogica(Long id_receita) {
        validar(id_receita);
        Receita receita = receitaRepository.getReferenceById(id_receita);
        receita.exclusaoLogica();
    }
    private void validar(Long id_receita) {
        if(!receitaRepository.existsById(id_receita)){
            throw new ValidacaoException("Não foi possivel encontra uma receita com esse id: " + id_receita);
        }
    }


    public List<ReceitaMesDto> listarMes(int ano, int mes) {
        List<ReceitaMesDto> lista = receitaRepository.listarPorMesEAno(ano,mes);
        if(lista.isEmpty()){
            throw new ValidacaoException("Não foi possivel encontrar nenhuma receita no banco de dados nessa data!");
        }
        return lista;
    }
}
