package financeiro.api.dto.receita;

import financeiro.api.model.receita.Receita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoReceitaDto(Long id_receita, String descricao, BigDecimal valor, LocalDateTime data) {
    public AtualizacaoReceitaDto(Receita receitaAtualizada) {
        this(receitaAtualizada.getId_receita(),receitaAtualizada.getDescricao(),receitaAtualizada.getValor(),receitaAtualizada.getData());
    }
}
