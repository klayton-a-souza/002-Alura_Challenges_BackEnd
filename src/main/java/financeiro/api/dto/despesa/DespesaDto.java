package financeiro.api.dto.despesa;

import com.fasterxml.jackson.annotation.JsonProperty;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaDto(

        @JsonProperty("id_despesa")
        Long id_despesa,
        @NotNull
        @JsonProperty("descricao")
        String descricao,
        @NotNull
        @JsonProperty("valor")
        BigDecimal valor,
        @NotNull
        @JsonProperty("data")
        LocalDateTime data,
        @JsonProperty("categoria")
        Categoria categoria,
        @JsonProperty("ativo")
        Boolean ativo) {

        public DespesaDto(Despesa despesa){
                this(despesa.getId_despesa(), despesa.getDescricao(), despesa.getValor(),despesa.getData(),despesa.getCategoria(),despesa.getAtivo());
        }
}
