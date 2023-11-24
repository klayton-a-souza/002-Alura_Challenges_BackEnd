package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Tipo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaDto(
        Long id_despesa,
        @NotNull
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        LocalDateTime data,
        @NotNull
        Tipo tipo) {
}
