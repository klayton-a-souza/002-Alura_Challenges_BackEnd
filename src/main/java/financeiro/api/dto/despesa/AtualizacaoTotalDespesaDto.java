package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoTotalDespesaDto(
        @NotNull
        Long id_despesa,
        @NotNull
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        LocalDateTime data,
        @NotNull
        Categoria categoria) {
    public AtualizacaoTotalDespesaDto(Despesa despesaAtualizada) {
        this(despesaAtualizada.getId_despesa(),despesaAtualizada.getDescricao(),despesaAtualizada.getValor(),despesaAtualizada.getData(),despesaAtualizada.getCategoria());
    }
}
