package financeiro.api.dto.receita;

import financeiro.api.model.receita.Receita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReceitaMesDto(String descricao, BigDecimal valor, LocalDateTime data) {

    public ReceitaMesDto(Receita receita) {
        this(receita.getDescricao(), receita.getValor(),receita.getData());
    }
}
