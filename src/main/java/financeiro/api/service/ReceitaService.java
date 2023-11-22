package financeiro.api.service;

import financeiro.api.dto.receita.CadastrarReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.Receita;
import financeiro.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    public Receita cadastrar(CadastrarReceitaDto dto) {
        Optional<Receita> receitaComMesmaDescricao = receitaRepository.getReferenceByDescricao(dto.descricao());
        if(!receitaComMesmaDescricao.isEmpty()){
            if(receitaComMesmaDescricao.get().getData().getMonthValue() == dto.data().getMonthValue()){
                throw new ValidacaoException("Encontramos uma receita com essa mesma descriçãore registrada no banco de dados!");
            }
        }
        return receitaRepository.save(new Receita(dto));
    }
}
