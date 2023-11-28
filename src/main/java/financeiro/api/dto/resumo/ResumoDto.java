package financeiro.api.dto.resumo;

import java.math.BigDecimal;

public record ResumoDto(BigDecimal receitaMes, BigDecimal despesaMes, BigDecimal saldo,
                        BigDecimal alimentação, BigDecimal saude, BigDecimal moradia, BigDecimal transporte,
                        BigDecimal educacao,BigDecimal lazer, BigDecimal imprevistos, BigDecimal outras) {
}
