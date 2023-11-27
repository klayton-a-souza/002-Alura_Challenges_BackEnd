package financeiro.api.dto.despesa;

import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
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
        Categoria categoria,
        Boolean ativo) {

        public DespesaDto(Despesa despesa){
                this(despesa.getId_despesa(), despesa.getDescricao(), despesa.getValor(),despesa.getData(),despesa.getCategoria(),despesa.getAtivo());
        }
}
