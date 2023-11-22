package financeiro.api.service;

import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.Receita;
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

    public List<ReceitaDto> listar(Pageable paginacao) {
        return receitaRepository
                .findAll(paginacao)
                .stream()
                .map(ReceitaDto::new)
                .toList();
    }
}
