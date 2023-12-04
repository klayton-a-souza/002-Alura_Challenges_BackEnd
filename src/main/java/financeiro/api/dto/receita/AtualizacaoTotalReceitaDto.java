package financeiro.api.dto.receita;

import financeiro.api.model.receita.Receita;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoTotalReceitaDto(
        Long id_receita,
        @NotNull
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        LocalDateTime data) {
    public AtualizacaoTotalReceitaDto(Receita receitaAtualizada) {
        this(receitaAtualizada.getId_receita(),receitaAtualizada.getDescricao(),receitaAtualizada.getValor(),receitaAtualizada.getData());
    }
}
