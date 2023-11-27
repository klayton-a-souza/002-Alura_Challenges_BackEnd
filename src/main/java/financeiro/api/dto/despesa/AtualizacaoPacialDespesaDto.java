package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoPacialDespesaDto(
        @NotNull
        Long id_despesa,
        String descricao,
        BigDecimal valor,
        LocalDateTime data,
        Categoria categoria) {
    public AtualizacaoPacialDespesaDto(Despesa despesaAtualizada) {
        this(despesaAtualizada.getId_despesa(),despesaAtualizada.getDescricao(),despesaAtualizada.getValor(),despesaAtualizada.getData(),despesaAtualizada.getCategoria());
    }
}
