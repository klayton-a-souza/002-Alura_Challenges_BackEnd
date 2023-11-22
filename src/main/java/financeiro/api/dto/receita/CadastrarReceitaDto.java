package financeiro.api.dto.receita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CadastrarReceitaDto(
        Long id_receita,
        String descricao,
        BigDecimal valor,
        LocalDateTime data,
        Boolean ativo) {
}
