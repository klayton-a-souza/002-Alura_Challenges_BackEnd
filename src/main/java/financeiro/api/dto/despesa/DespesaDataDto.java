package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaDataDto(String descricao, BigDecimal valor, LocalDateTime data, Categoria categoria) {

    public DespesaDataDto(Despesa despesa){
        this(despesa.getDescricao(),despesa.getValor(),despesa.getData(),despesa.getCategoria());
    }
}
