package financeiro.api.service;

import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public Despesa cadastrar(DespesaDto dto) {
        verificarDespesa(dto);
        return despesaRepository.save(new Despesa(dto));
    }

    private void verificarDespesa(DespesaDto dto) {
        //Procurar no banco de dados se existe uma receita com a mesma descrição recebido no dto e se essa receita esta no mesmo mês
        Optional<Despesa> despesa = despesaRepository.getReferenceByDescricao(dto.descricao());
        if(!despesa.isEmpty()){
            var mesDespesaBanco = despesa.get().getData().getMonthValue();
            var mesDto = dto.data().getMonthValue();
            if(mesDespesaBanco == mesDto){
                throw new ValidacaoException("A API não permiti o cadsatrao de despesas duplicaads(contendo a mesma descricao, dentro do mesmo mês)");
            }
        }
    }
}
