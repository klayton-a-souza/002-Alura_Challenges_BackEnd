package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Despesa;
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
        Tipo tipo,
        Boolean ativo) {

        public DespesaDto(Despesa despesa){
                this(despesa.getId_despesa(), despesa.getDescricao(), despesa.getValor(),despesa.getData(),despesa.getTipo(),despesa.getAtivo());
        }
}
