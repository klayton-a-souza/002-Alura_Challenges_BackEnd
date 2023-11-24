package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Despesa;
import financeiro.api.model.despesa.Tipo;
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
        Tipo tipo) {
    public AtualizacaoTotalDespesaDto(Despesa despesaAtualizada) {
        this(despesaAtualizada.getId_despesa(),despesaAtualizada.getDescricao(),despesaAtualizada.getValor(),despesaAtualizada.getData(),despesaAtualizada.getTipo());
    }
}
