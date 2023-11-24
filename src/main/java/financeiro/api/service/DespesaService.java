package financeiro.api.service;

import financeiro.api.dto.despesa.AtualizacaoPacialDespesaDto;
import financeiro.api.dto.despesa.AtualizacaoTotalDespesaDto;
import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.model.despesa.Tipo;
import financeiro.api.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public Despesa cadastrar(DespesaDto dto) {
        verificarDespesa(dto);
        return despesaRepository.save(new Despesa(dto));
    }

    public List<DespesaDto> listar(Pageable paginacao) {
        return despesaRepository.findAllByAtivoTrue(paginacao).stream().map(DespesaDto::new).toList();
    }

    public Despesa detalhar(Long id_despesa) {
        validar(id_despesa);
        return despesaRepository.getReferenceById(id_despesa);
    }

    public Despesa parcial(AtualizacaoPacialDespesaDto dto) {
        validar(dto.id_despesa());
        Despesa despesa = despesaRepository.getReferenceById(dto.id_despesa());
        despesa.parcial(dto);
        return despesa;
    }
    public Despesa total(AtualizacaoTotalDespesaDto dto) {
        validar(dto.id_despesa());
        Despesa despesa = despesaRepository.getReferenceById(dto.id_despesa());
        despesa.total(dto);
        return despesa;
    }

    public void exclusaoLogica(Long id_despesa) {
        validar(id_despesa);
        Despesa despesa = despesaRepository.getReferenceById(id_despesa);
        despesa.exclusaoLogica();
    }

    private void validar(Long id_despesa) {
        if(!despesaRepository.existsById(id_despesa)){
            throw new ValidacaoException("Não foi possivel encontrar uma despesa com esse id: " + id_despesa);
        }
    }

    private void verificarDespesa(DespesaDto dto) {
        //Procurar no banco de dados se existe uma receita com a mesma descrição recebido no dto e se essa receita esta no mesmo mês
        Optional<Despesa> despesa = despesaRepository.getReferenceByDescricao(dto.descricao());
        if(!despesa.isEmpty()){

            var mesDespesaBanco = despesa.get().getData().getMonthValue() == dto.data().getMonthValue();
            var tipoFixo = dto.tipo() == Tipo.FIXA;

            if(mesDespesaBanco && tipoFixo){
                throw new ValidacaoException("A API não permiti o cadsatrao de despesas duplicaads(contendo a mesma descricao, dentro do mesmo mês)");
            }
        }
    }
}
