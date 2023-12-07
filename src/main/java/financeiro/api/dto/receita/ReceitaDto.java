package financeiro.api.dto.receita;

import financeiro.api.model.receita.Receita;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReceitaDto(
        Long id_receita,
        @NotNull
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        LocalDateTime data,
        Boolean ativo) {

        public ReceitaDto(Receita receita){
                this(receita.getId_receita(), receita.getDescricao(), receita.getValor(),receita.getData(), receita.getAtivo());
        }
}
