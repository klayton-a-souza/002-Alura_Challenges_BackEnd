package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Despesa;
import financeiro.api.model.despesa.Tipo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoDespesaDto(
        @NotNull
        Long id_despesa,
        String descricao,
        BigDecimal valor,
        LocalDateTime data,
        Tipo tipo) {
    public AtualizacaoDespesaDto(Despesa despesaAtualizada) {
        this(despesaAtualizada.getId_despesa(),despesaAtualizada.getDescricao(),despesaAtualizada.getValor(),despesaAtualizada.getData(),despesaAtualizada.getTipo());
    }
}
